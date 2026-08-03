package com.tragepro.api.strategy.internal.executor;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;

public interface StrategyExecutor {
  StrategyResponse execute(StrategyRequest strategyRequest);
}
