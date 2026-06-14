package com.tragepro.api.data.repository;

import com.tragepro.api.data.model.entity.WatchListEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface WatchListRepository extends MongoRepository<WatchListEntity, String> {

  @Query(value = "{}", fields = "{ 'id': 1, 'name': 1, 'description': 1 }")
  Page<WatchListEntity> getWatchListSummery(Pageable pageable);
}
