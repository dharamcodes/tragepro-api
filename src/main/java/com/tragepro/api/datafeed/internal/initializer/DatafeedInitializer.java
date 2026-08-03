package com.tragepro.api.datafeed.internal.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tragepro.api.common.constant.DatafeedState;
import com.tragepro.api.common.model.DatafeedModel;
import com.tragepro.api.common.util.JsonReader;
import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.datafeed.internal.context.DatafeedContext;
import com.tragepro.api.datafeed.internal.context.WatchlistContext;
import com.tragepro.api.datafeed.internal.service.CandleService;
import com.tragepro.api.datafeed.internal.service.WatchListService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatafeedInitializer implements CommandLineRunner {

  private final WatchListService watchListService;
  private final CandleService candleService;
  private final WatchlistContext watchlistContext;
  private final DatafeedContext datafeedContext;

  @Override
  public void run(String... args) throws Exception {
    initializeWatchlist();
    initializeCandleData();
    populateContextFromServices();
  }

  private void initializeWatchlist() {
    List<WatchListRequest> watchListResponses =
        JsonReader.readJson(
            "__files/watchlists.json", new TypeReference<List<WatchListRequest>>() {});
    if (watchListResponses != null && !watchListResponses.isEmpty()) {
      watchListResponses.forEach(
          watchListRequest -> {
            var watchListResponse = watchListService.create(watchListRequest);
            watchlistContext.addWatchlist(watchListResponse.name(), watchListResponse.stocks());
          });
    }
  }

  private void initializeCandleData() {
    List<CandleRequest> candles =
        JsonReader.readJson("__files/candles.json", new TypeReference<List<CandleRequest>>() {});
    if (candles != null && !candles.isEmpty()) {
      candles.forEach(
          candle -> {
            if (!candleService.isCandleExists(
                candle.symbolData().name(), candle.candleData().timestamp())) {
              candleService.create(candle);
            }
          });
    }
  }

  private void populateContextFromServices() {
    try {
      var watchlists = watchListService.getAll();
      if (watchlists != null) {
        watchlists.forEach(
            wl -> {
              if (wl != null && wl.name() != null) {
                watchlistContext.addWatchlist(wl.name(), wl.stocks());
                if (wl.stocks() != null) {
                  wl.stocks()
                      .forEach(
                          stock ->
                              datafeedContext.put(
                                  stock,
                                  DatafeedModel.builder()
                                      .symbol(stock.symbol())
                                      .state(DatafeedState.INITIALIZED)
                                      .build()));
                }
              }
            });
      }
    } catch (Exception ignored) {
    }
  }
}
