package com.tragepro.api.datafeed.repository;

import com.tragepro.api.common.model.entity.CandleEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<CandleEntity, String> {
  boolean existsBySymbolDataNameAndCandleDataTimestamp(String name, long timestamp);

  List<CandleEntity> findBySymbolDataNameAndCandleDataTimestampGreaterThanEqual(
      String name, long timestamp);

  @Aggregation(
      pipeline = {
        "{ '$match': { 'symbolData.symbol': { '$in': ?0 } } }",
        "{ '$sort': { 'candleData.timestamp': -1 } }",
        "{ '$group': { '_id': '$symbolData.symbol', 'latestDoc': { '$first': '$$ROOT' } } }",
        "{ '$replaceRoot': { 'newRoot': '$latestDoc' } }"
      })
  List<CandleEntity> findLatestCandlesBySymbols(Collection<String> symbols);
}
