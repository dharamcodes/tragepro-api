package com.tragepro.api.marketdata.candle.repository;

import com.tragepro.api.marketdata.candle.model.entity.CandleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CandleRepository extends MongoRepository<CandleEntity, String> {}
