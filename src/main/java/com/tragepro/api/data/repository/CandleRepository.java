package com.tragepro.api.data.repository;

import com.tragepro.api.common.model.entity.CandleEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<CandleEntity, String> {
  boolean existsBySymbolDataNameAndCandleDataTimestamp(String name, long timestamp);

  List<CandleEntity> findBySymbolDataNameAndCandleDataTimestampGreaterThanEqual(
      String name, long timestamp);
}
