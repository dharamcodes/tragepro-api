package com.tragepro.api.identity.internal.repository;

import com.tragepro.api.identity.internal.entity.AccountDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDetailRepository extends MongoRepository<AccountDetailEntity, String> {
  AccountDetailEntity findByIdentifier(String identifier);

  AccountDetailEntity findByEmailAndIsActive(String email, boolean active);
}
