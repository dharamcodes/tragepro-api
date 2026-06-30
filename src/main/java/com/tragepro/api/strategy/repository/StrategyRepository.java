package com.tragepro.api.strategy.repository;

import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyRepository extends MongoRepository<StrategyEntity, String> {
  Optional<StrategyEntity> findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
      String watchlist, String symbol, StrategyState state);
}
