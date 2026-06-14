package com.tragepro.api.data.repository;

import com.tragepro.api.data.model.entity.SecurityEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SecurityRepository extends MongoRepository<SecurityEntity, String> {
  SecurityEntity findBySymbol(String symbol);
}
