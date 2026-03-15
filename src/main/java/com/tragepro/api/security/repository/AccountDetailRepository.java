package com.tragepro.api.security.repository;

import com.tragepro.api.security.model.entity.AccountDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDetailRepository extends MongoRepository<AccountDetailEntity, String> {
    AccountDetailEntity findByIdentifier(String identifier);

    AccountDetailEntity findByEmailAndIsActive(String email, boolean active);
}
