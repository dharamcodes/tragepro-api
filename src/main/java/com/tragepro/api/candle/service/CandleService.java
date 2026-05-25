package com.tragepro.api.candle.service;

import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.model.response.CandleResponse;
import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandleService {

    /**
     * Persists a new candle record.
     *
     * @param candleRequest payload containing symbol and candle data
     * @return {@link CandleResponse} with the saved record including its generated ID
     */
    CandleResponse create(CandleRequest candleRequest);

    /**
     * Retrieves a single candle by its MongoDB document ID.
     *
     * @param id the MongoDB document ID of the record
     * @return {@link CandleResponse} for the matched record
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no record exists for the given ID
     */
    CandleResponse getById(String id);

    /**
     * Returns a paginated list of all candles as lightweight summary projections.
     * An empty collection returns an empty page — not an error.
     *
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of {@link CandleSummaryResponse}, may be empty
     */
    Page<CandleSummaryResponse> getAll(Pageable pageable);

    /**
     * Returns a paginated list of candles for a specific symbol.
     * Results are ordered by timestamp descending (newest first).
     * Uses the {@code symbol_timestamp_idx} compound index — no collection scan.
     *
     * @param symbolId the symbol identifier to filter by
     * @param pageable pagination and sorting parameters
     * @return a {@link Page} of {@link CandleSummaryResponse} for the given symbol
     */
    Page<CandleSummaryResponse> getBySymbol(String symbolId, Pageable pageable);

    /**
     * Returns all candles for a symbol within a time range.
     * Uses the {@code symbol_timestamp_idx} compound index for efficient range scans.
     *
     * @param symbolId      the symbol identifier to filter by
     * @param fromTimestamp start of the time range as epoch milliseconds (inclusive)
     * @param toTimestamp   end of the time range as epoch milliseconds (inclusive)
     * @return list of {@link CandleSummaryResponse} ordered by timestamp ascending
     */
    List<CandleSummaryResponse> getBySymbolAndTimeRange(String symbolId, long fromTimestamp, long toTimestamp);

    /**
     * Returns the most recent candle for every distinct symbol.
     * Uses a MongoDB aggregation pipeline with {@code $sort + $group + $first}
     * backed by the {@code symbol_timestamp_idx}.
     *
     * @return list of {@link CandleSummaryResponse}, one per symbol
     */
    List<CandleSummaryResponse> getLatestPerSymbol();

    /**
     * Returns the most recent candle for a specific list of symbols.
     * Uses a MongoDB aggregation pipeline backed by the {@code symbol_timestamp_idx}.
     *
     * @param symbols list of symbol IDs to filter by
     * @return list of {@link CandleSummaryResponse}, one per requested symbol
     */
    List<CandleSummaryResponse> getLatestForSymbols(List<String> symbols);

    /**
     * Replaces the fields of an existing candle record using a merge strategy.
     *
     * @param id            the MongoDB document ID of the record to update
     * @param candleRequest updated symbol and candle data
     * @return {@link CandleResponse} reflecting the updated state
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no record exists for the given ID
     */
    CandleResponse update(String id, CandleRequest candleRequest);

    /**
     * Permanently deletes a candle record from the database.
     *
     * @param id the MongoDB document ID of the record to delete
     * @throws com.tragepro.api.exception.AppException DATA_NOT_FOUND if no record exists for the given ID
     */
    void delete(String id);
}
