package com.tragepro.api.candle.repository;

import com.tragepro.api.candle.model.entity.CandleEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<CandleEntity, String> {

    Page<CandleEntity> findBySymbolId(String symbolId, Pageable pageable);

    List<CandleEntity> findBySymbolIdAndCandleTimestampBetweenOrderByCandleTimestampAsc(
            String symbolId, long fromTime, long toTime);

    Page<CandleEntity> findByCandleTimestampBetween(long fromTime, long toTime, Pageable pageable);

    boolean existsBySymbolIdAndCandleTimestamp(String symbolId, long timestamp);
}
