package com.tragepro.api.security.repository;

import com.tragepro.api.security.model.entity.AuthenticationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthenticationRepository extends MongoRepository<AuthenticationEntity, String> {

    AuthenticationEntity findByUserName(String userName);

    AuthenticationEntity findByUserNameAndIsActive(String userName, boolean isActive);
}
