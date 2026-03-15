package io.tragepro.api.security.repository;

import io.tragepro.api.security.model.entity.AccountDetailEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountDetailRepository extends MongoRepository<AccountDetailEntity, String> {
    AccountDetailEntity findByIdentifier(String identifier);

    AccountDetailEntity findByEmailAndIsActive(String email, boolean active);
}
