package com.tragepro.api.datafeed.repository;

import com.tragepro.api.datafeed.model.entity.SecurityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecurityRepository extends MongoRepository<SecurityEntity, String> {
  SecurityEntity findBySymbol(String symbol);
}
