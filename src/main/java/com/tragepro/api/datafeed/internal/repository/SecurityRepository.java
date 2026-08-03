package com.tragepro.api.datafeed.internal.repository;

import com.tragepro.api.datafeed.internal.entity.SecurityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecurityRepository extends MongoRepository<SecurityEntity, String> {
  SecurityEntity findBySymbol(String symbol);
}
