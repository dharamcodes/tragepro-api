package com.tragepro.api.datafeed.internal.adapter;

import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.DatafeedAdapter;
import com.tragepro.api.datafeed.dto.CandleResponse;
import com.tragepro.api.datafeed.dto.SecurityResponse;
import com.tragepro.api.datafeed.internal.context.WatchlistContext;
import com.tragepro.api.datafeed.internal.service.CandleService;
import com.tragepro.api.datafeed.internal.service.SecurityService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Inter-module adapter implementation delegating datafeed queries to internal services. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatafeedAdapterImpl implements DatafeedAdapter {

  private final CandleService candleService;
  private final SecurityService securityService;
  private final WatchlistContext watchlistContext;

  /**
   * Retrieves symbols for a named watchlist from the watchlist context.
   *
   * @param name watchlist name
   * @return set of symbol models
   */
  @Override
  public Set<SymbolDataModel> getWatchlistSymbols(String name) {
    log.info("Adapter fetching watchlist symbols for name: {}", name);
    return watchlistContext.getWatchlist(name);
  }

  /**
   * Retrieves historical candles for a symbol over a specified number of days back.
   *
   * @param symbolName symbol name
   * @param daysBack number of days back
   * @return list of candle responses
   */
  @Override
  public List<CandleResponse> getCandlesBySymbolAndDaysBack(String symbolName, int daysBack) {
    log.info("Adapter fetching candles for symbol: {}, daysBack: {}", symbolName, daysBack);
    return candleService.getCandlesBySymbolAndDaysBack(symbolName, daysBack);
  }

  /**
   * Retrieves the latest candle for each provided symbol.
   *
   * @param symbols set of symbols
   * @return set of latest candle responses
   */
  @Override
  public Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols) {
    log.info("Adapter fetching latest candles for symbols: {}", symbols);
    return candleService.getLatestCandlesBySymbols(symbols);
  }

  /**
   * Retrieves security metadata by symbol code.
   *
   * @param symbol symbol code
   * @return security response
   */
  @Override
  public SecurityResponse getSecurityBySymbol(String symbol) {
    log.info("Adapter fetching security details for symbol: {}", symbol);
    return securityService.fetSecurityBySymbol(symbol);
  }
}
