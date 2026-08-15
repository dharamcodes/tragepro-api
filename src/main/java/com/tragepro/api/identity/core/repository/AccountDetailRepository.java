package com.tragepro.api.identity.core.repository;

import com.tragepro.api.domain.identity.entity.AccountDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDetailRepository extends MongoRepository<AccountDetailEntity, String> {
  AccountDetailEntity findByIdentifier(String identifier);

  AccountDetailEntity findByEmailAndIsActive(String email, boolean active);
}
