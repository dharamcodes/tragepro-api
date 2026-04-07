package com.tragepro.api.ohlcvdata.repository;

import com.tragepro.api.ohlcvdata.model.entity.OHLCVDataEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OHLCVDataRepository extends MongoRepository<OHLCVDataEntity, String> {}
