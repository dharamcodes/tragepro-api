package com.tragepro.api.watchlist.repository;

import com.tragepro.api.watchlist.model.entity.WatchlistEntity;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchlistRepository extends MongoRepository<WatchlistEntity, String> {
    List<WatchlistEntity> findByUserId(String userId);
}
