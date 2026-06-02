package com.tragepro.api.data.repository;

import com.tragepro.api.data.model.entity.WatchListEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WatchListRepository extends MongoRepository<WatchListEntity, String> {}
