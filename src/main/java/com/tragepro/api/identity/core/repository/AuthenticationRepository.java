package com.tragepro.api.identity.core.repository;

import com.tragepro.api.domain.identity.entity.AuthenticationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticationRepository extends MongoRepository<AuthenticationEntity, String> {

    AuthenticationEntity findByUserName(String userName);

    AuthenticationEntity findByUserNameAndIsActive(String userName, Boolean isActive);
}
