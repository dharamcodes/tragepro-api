package com.tragepro.api.data.repository;

import com.tragepro.api.data.model.entity.CandleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<CandleEntity, String> {
  boolean existsBySymbolDataNameAndCandleDataTimestamp(String name, long timestamp);
}
