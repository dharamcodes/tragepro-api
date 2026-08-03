package com.tragepro.api.datafeed;

import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.datafeed.dto.CandleResponse;
import com.tragepro.api.datafeed.dto.SecurityResponse;
import java.util.List;
import java.util.Set;

/** Public facade interface for inter-module communication with the Datafeed bounded context. */
public interface DatafeedAdapter {

  /**
   * Retrieves symbols for a named watchlist.
   *
   * @param name watchlist name
   * @return set of symbol models contained within the watchlist
   */
  Set<SymbolDataModel> getWatchlistSymbols(String name);

  /**
   * Retrieves historical candles for a symbol over a specified number of days back.
   *
   * @param symbolName symbol name
   * @param daysBack number of days back from present time
   * @return list of candle responses
   */
  List<CandleResponse> getCandlesBySymbolAndDaysBack(String symbolName, int daysBack);

  /**
   * Retrieves the latest candle for each provided symbol.
   *
   * @param symbols set of symbols to query
   * @return set of latest candle responses
   */
  Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols);

  /**
   * Retrieves security metadata by symbol code.
   *
   * @param symbol symbol code
   * @return security response containing identifier and name
   */
  SecurityResponse getSecurityBySymbol(String symbol);
}
