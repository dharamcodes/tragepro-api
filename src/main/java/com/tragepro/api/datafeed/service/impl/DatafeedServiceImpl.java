package com.tragepro.api.datafeed.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.datafeed.core.context.DatafeedContext;
import com.tragepro.api.datafeed.core.feed.FeedAdapterFactory;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.datafeed.service.DatafeedService;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.DatafeedModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DatafeedState;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DatafeedServiceImpl implements DatafeedService {

  private final WatchListService watchListService;
  private final SecurityService securityService;
  private final CandleService candleService;
  private final FeedAdapterFactory feedAdapterFactory;
  private final DatafeedContext datafeedContext;
  private final Executor datafeedThreadPoolExecutor;

  @Value("${data.fetch.max-concurrency:10}")
  private int maxConcurrency = 10;

  public DatafeedServiceImpl(
      WatchListService watchListService,
      SecurityService securityService,
      CandleService candleService,
      FeedAdapterFactory feedAdapterFactory,
      DatafeedContext datafeedContext,
      @Qualifier("applicationTaskExecutor") Executor datafeedThreadPoolExecutor) {
    this.watchListService = watchListService;
    this.securityService = securityService;
    this.candleService = candleService;
    this.feedAdapterFactory = feedAdapterFactory;
    this.datafeedContext = datafeedContext;
    this.datafeedThreadPoolExecutor = datafeedThreadPoolExecutor;
  }

  @Override
  public LoadCandleResponse loadData(LoadCandleRequest request) {
    if (request == null || request.watchListName() == null) {
      log.error("Invalid load request");
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }

    var watchlist =
        watchListService.getAll().stream()
            .filter(w -> w.name().equalsIgnoreCase(request.watchListName()))
            .findFirst()
            .orElseThrow(
                () -> {
                  log.error("Watchlist not found :: {}", request.watchListName());
                  return new AppException(ErrorType.DATA_NOT_FOUND);
                });

    var stocks = watchlist.stocks();
    if (stocks == null || stocks.isEmpty()) {
      log.info("No stocks defined in watchlist :: {}", request.watchListName());
      return LoadCandleResponse.builder()
          .watchList(request.watchListName())
          .message("No symbols found in watchlist to process")
          .build();
    }
    datafeedThreadPoolExecutor.execute(() -> asyncDataLoad(stocks, request.daysBack()));

    return LoadCandleResponse.builder()
        .watchList(request.watchListName())
        .message("Data load initiated successfully")
        .build();
  }

  private void asyncDataLoad(Set<SymbolDataModel> stocks, int daysBack) {
    log.info(
        "Starting asynchronous data load for {} symbols with daysBack={}", stocks.size(), daysBack);

    stocks.forEach(
        stock -> {
          try {
            log.info("Processing data load for stock symbol: {}", stock.symbol());

            SecurityResponse security;
            try {
              security = securityService.fetSecurityBySymbol(stock.symbol());
            } catch (Exception ex) {
              log.warn("Security not found for symbol: {}, skipping", stock.symbol(), ex);
              return;
            }

            updateContextState(stock, DatafeedState.PROCESSING, null);

            var clientReq =
                FeedClientRequest.builder()
                    .securityId(security.securityId())
                    .instrument(security.symbol())
                    .fromDate(LocalDate.now().minusDays(daysBack).toString())
                    .toDate(LocalDate.now().toString())
                    .build();

            var candles = feedAdapterFactory.get().intradayDataAdapter(clientReq);
            log.info(
                "Retrieved {} candles from adapter for symbol: {}", candles.size(), stock.symbol());

            var enrichedSymbol =
                SymbolDataModel.builder().symbol(stock.symbol()).name(stock.name()).build();

            processCandlesParallel(candles, enrichedSymbol, stock);
            var latestDate =
                candles.stream().mapToLong(candle -> candle.candleData().timestamp()).max().stream()
                    .mapToObj(
                        timestamp ->
                            timestamp > 1_000_000_000_000L
                                ? Instant.ofEpochMilli(timestamp)
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate()
                                : LocalDate.ofEpochDay(timestamp))
                    .findFirst()
                    .orElse(LocalDate.now());

            updateContextState(stock, DatafeedState.COMPLETED, latestDate);
            log.info("Data load completed successfully for symbol: {}", stock.symbol());

          } catch (Exception e) {
            log.error("Failed to perform data load for symbol: {}", stock.symbol(), e);
            try {
              updateContextState(stock, DatafeedState.INITIALIZED, null);
            } catch (Exception ex) {
              log.error("Failed to revert state for symbol: {}", stock.symbol(), ex);
            }
          }
        });
    log.info("Asynchronous data load process finished.");
  }

  private void updateContextState(SymbolDataModel key, DatafeedState state, LocalDate timestamp) {
    Optional.ofNullable(datafeedContext.get(key))
        .ifPresentOrElse(
            contextModel -> {
              if (timestamp != null) {
                contextModel.setTimestamp(timestamp);
              }
              log.info("updating the data-feed context for symbol: {}", contextModel.getSymbol());
              datafeedContext.updateStatus(key, state);
            },
            () ->
                datafeedContext.put(
                    key,
                    DatafeedModel.builder()
                        .symbol(key.symbol())
                        .timestamp(timestamp)
                        .state(state)
                        .build()));
  }

  private void processCandlesParallel(
      List<CandleRequest> candles, SymbolDataModel enrichedSymbol, SymbolDataModel stock) {
    try (var executor =
        java.util.concurrent.Executors.newFixedThreadPool(
            maxConcurrency, Thread.ofVirtual().factory())) {
      candles.forEach(
          candle ->
              executor.submit(
                  () -> {
                    long timestamp = candle.candleData().timestamp();
                    if (!candleService.isCandleExists(enrichedSymbol.name(), timestamp)) {
                      var enriched =
                          CandleRequest.builder()
                              .symbolData(enrichedSymbol)
                              .candleData(candle.candleData())
                              .build();
                      log.info("Loading symbol:: {} timestamp:: {}", stock.symbol(), timestamp);
                      candleService.create(enriched);
                    }
                  }));
    }
  }
}
