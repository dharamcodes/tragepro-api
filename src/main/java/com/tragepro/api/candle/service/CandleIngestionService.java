package com.tragepro.api.candle.service;

import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.model.request.CandleRequest;
import java.util.List;

public interface CandleIngestionService {

    /**
     * Fetches all configured symbols from the data provider and persists them
     * using a bulk upsert operation. Duplicate candles (same symbol + timestamp)
     * are updated rather than inserted.
     *
     * @param interval the candle interval to request from the provider
     */
    void ingestAll(CandleInterval interval);

    /**
     * Performs a bulk upsert of a pre-assembled list of candle records.
     * Useful for manual ingestion or testing without invoking the provider.
     *
     * @param records list of candle records to persist
     * @return number of records inserted or updated
     */
    int bulkUpsert(List<CandleRequest> records);
}
