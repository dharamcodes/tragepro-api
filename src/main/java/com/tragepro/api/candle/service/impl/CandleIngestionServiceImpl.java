package com.tragepro.api.candle.service.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.provider.DataProviderClient;
import com.tragepro.api.candle.service.CandleIngestionService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandleIngestionServiceImpl implements CandleIngestionService {

    private final DataProviderClient dataProviderClient;
    private final MongoTemplate mongoTemplate;

    @Override
    public void ingestAll(CandleInterval interval) {
        log.info("Starting ingestion cycle — interval: {}", interval.getValue());
        long start = System.currentTimeMillis();
        List<CandleRequest> records = dataProviderClient.fetchAll(interval);
        if (records.isEmpty()) {
            log.warn("Provider returned no records for interval: {}", interval.getValue());
            return;
        }
        int affected = bulkUpsert(records);
        log.info(
                "Ingestion complete — {} symbols processed, {} documents affected in {}ms",
                records.size(),
                affected,
                System.currentTimeMillis() - start);
    }

    @Override
    public int bulkUpsert(List<CandleRequest> records) {
        if (records.isEmpty()) {
            return 0;
        }

        BulkOperations ops = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, CandleEntity.class);

        for (CandleRequest req : records) {
            Query matchKey = new Query(where("symbol.id")
                    .is(req.getSymbol().id())
                    .and("candle.timestamp")
                    .is(req.getCandle().timestamp()));

            Update update = new Update()
                    .set("symbol", req.getSymbol())
                    .set("candle", req.getCandle())
                    .setOnInsert("createdAt", Instant.now());

            ops.upsert(matchKey, update);
        }

        var result = ops.execute();
        int inserted = result.getInsertedCount() + result.getUpserts().size();
        int modified = result.getModifiedCount();
        log.debug("Bulk upsert result — inserted/upserted: {}, modified: {}", inserted, modified);
        return inserted + modified;
    }
}
