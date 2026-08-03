package com.tragepro.api.datafeed.internal.service;

import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.CandleResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Service interface managing market price candles and queries. */
public interface CandleService {

  /**
   * Creates a new market price candle entry.
   *
   * @param candleRequest request payload containing symbol and OHLCV data
   * @return response representing the created candle
   */
  CandleResponse create(CandleRequest candleRequest);

  /**
   * Retrieves a candle entry by its unique identifier.
   *
   * @param id candle identifier
   * @return optional containing the candle response if found
   */
  Optional<CandleResponse> getById(String id);

  /**
   * Retrieves a paginated list of all candles.
   *
   * @param pageable pagination parameters
   * @return page of candle responses
   */
  Page<CandleResponse> getAll(Pageable pageable);

  /**
   * Retrieves a set of all candles.
   *
   * @return set of candle responses
   */
  Set<CandleResponse> getAll();

  /**
   * Updates an existing candle entry by identifier.
   *
   * @param id candle identifier
   * @param candleRequest updated candle payload
   * @return updated candle response
   */
  CandleResponse update(String id, CandleRequest candleRequest);

  /**
   * Deletes a candle entry by identifier.
   *
   * @param id candle identifier
   */
  void delete(String id);

  /**
   * Checks if a candle entry exists for a symbol name and timestamp.
   *
   * @param name symbol name
   * @param timestamp epoch timestamp
   * @return true if candle exists, false otherwise
   */
  boolean isCandleExists(String name, long timestamp);

  /**
   * Retrieves historical candles for a symbol over a specified number of days back.
   *
   * @param symbolName symbol name
   * @param daysBack number of days back
   * @return list of candle responses
   */
  List<CandleResponse> getCandlesBySymbolAndDaysBack(String symbolName, int daysBack);

  /**
   * Retrieves the latest candle entry for each provided symbol.
   *
   * @param symbols set of symbol names
   * @return set of latest candle responses
   */
  Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols);
}
