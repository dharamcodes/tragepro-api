package com.tragepro.api.datafeed.initializer;

import com.tragepro.api.common.constant.DatafeedState;
import com.tragepro.api.common.context.DatafeedContext;
import com.tragepro.api.common.context.WatchlistContext;
import com.tragepro.api.common.model.DatafeedModel;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.common.model.response.CandleResponse;
import com.tragepro.api.datafeed.model.response.WatchListResponse;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.datafeed.service.WatchListService;
import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatafeedInitializer implements CommandLineRunner {

  private final WatchListService watchListService;
  private final WatchlistContext watchlistContext;

  private final CandleService candleService;
  private final DatafeedContext datafeedContext;

  @Override
  public void run(String @NonNull ... args) throws Exception {
    Set<WatchListResponse> watchListResponses = watchListService.getAll();
    log.info("Fetched watchlist count :: {}", watchListResponses.size());
    watchlistInitializer(watchListResponses);

    Set<String> symbols =
        watchListResponses.stream()
            .flatMap(
                watchListResponse ->
                    watchListResponse.stocks().stream().map(SymbolDataModel::symbol))
            .collect(Collectors.toSet());
    Set<CandleResponse> candleResponses = candleService.getLatestCandlesBySymbols(symbols);
    log.info("Fetched candles count :: {}", watchListResponses.size());
    candleDataInitializer(candleResponses);
  }

  private void watchlistInitializer(Set<WatchListResponse> watchlist) {
    watchlist.forEach(
        watchListResponse -> {
          log.info("Initialized Watchlist :: {}", watchListResponse.name());
          watchlistContext.addWatchlist(watchListResponse.name(), watchListResponse.stocks());
        });
    log.info("Initialized Watchlist - Count :: {}", watchlist.size());
  }

  private void candleDataInitializer(Set<CandleResponse> candles) {
    candles.forEach(
        candle -> {
          log.info("Initialized candle for symbol :: {}", candle.symbolData().name());
          datafeedContext.put(
              candle.symbolData(),
              DatafeedModel.builder()
                  .symbol(candle.symbolData().name())
                  .timestamp(LocalDate.ofEpochDay(candle.candleData().timestamp()))
                  .state(DatafeedState.INITIALIZED)
                  .build());
        });
    log.info("Initialized Candles - Count :: {}", candles.size());
  }
}
