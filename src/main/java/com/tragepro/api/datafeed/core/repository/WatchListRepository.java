package com.tragepro.api.datafeed.core.repository;

import com.tragepro.api.domain.datafeed.entity.WatchListEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface WatchListRepository extends MongoRepository<WatchListEntity, String> {

  @Query(value = "{}", fields = "{ 'id': 1, 'name': 1, 'description': 1 }")
  Page<WatchListEntity> getWatchListSummery(Pageable pageable);

  List<WatchListEntity> findAll();
}
