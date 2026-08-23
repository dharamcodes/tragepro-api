package com.tragepro.api.strategy.core.repository;

import com.tragepro.api.domain.strategy.constant.StrategyState;
import com.tragepro.api.domain.strategy.entity.StrategyEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StrategyRepository extends MongoRepository<StrategyEntity, String> {
    Optional<StrategyEntity> findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
            String watchlist, String symbol, StrategyState state);
}
