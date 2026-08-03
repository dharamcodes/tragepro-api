package com.tragepro.api.common;

import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.model.CandleDataModel;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.FeedClientRequest;
import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.strategy.dto.StatusModel;
import com.tragepro.api.strategy.dto.StrategyModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.SymbolModel;
import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.constant.StrategyStep;
import java.util.Set;

public final class MockDataFactory {

  private MockDataFactory() {}

  public static SymbolDataModel createSymbolData(String symbol, String name) {
    return new SymbolDataModel(symbol, name);
  }

  public static CandleDataModel createCandleData(
      long timestamp, double open, double high, double low, double close, long volume) {
    return new CandleDataModel(timestamp, open, high, low, close, volume);
  }

  public static CandleRequest createCandleRequest(String symbol, String name, long timestamp) {
    return new CandleRequest(
        createSymbolData(symbol, name),
        createCandleData(timestamp, 150.0, 155.0, 149.0, 154.0, 5000L));
  }

  public static WatchListRequest createWatchListRequest(String name, SymbolDataModel... stocks) {
    return WatchListRequest.builder()
        .name(name)
        .description("Test Watchlist " + name)
        .stocks(Set.of(stocks))
        .build();
  }

  public static FeedClientRequest createFeedClientRequest(int securityId, String instrument) {
    return FeedClientRequest.builder()
        .securityId(securityId)
        .instrument(instrument)
        .fromDate("2026-01-01")
        .toDate("2026-01-02")
        .build();
  }

  public static StrategyRequest createStrategyRequest(
      String strategyName, String symbol, String watchlist) {
    return StrategyRequest.builder()
        .strategy(
            StrategyModel.builder()
                .name(strategyName)
                .desc("Description " + strategyName)
                .watchlist(watchlist)
                .build())
        .symbolData(
            SymbolModel.builder()
                .symbol(symbol)
                .name(symbol + " Inc.")
                .exchange(Exchange.NSE)
                .build())
        .currentState(
            StatusModel.builder().state(StrategyState.INITIALIZING).step(StrategyStep.INIT).build())
        .build();
  }
}
