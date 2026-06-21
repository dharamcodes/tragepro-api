package com.tragepro.api.strategy.executor;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyExecutor {
  StrategyResponse execute(StrategyRequest request);
}
