package com.tragepro.api.strategy.internal.repository;

import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.entity.StrategyEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyRepository extends MongoRepository<StrategyEntity, String> {
  Optional<StrategyEntity> findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
      String watchlist, String symbol, StrategyState state);
}
