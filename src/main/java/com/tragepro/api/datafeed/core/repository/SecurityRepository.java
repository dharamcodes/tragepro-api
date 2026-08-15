package com.tragepro.api.datafeed.core.repository;

import com.tragepro.api.domain.datafeed.entity.SecurityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecurityRepository extends MongoRepository<SecurityEntity, String> {
  SecurityEntity findBySymbol(String symbol);
}
