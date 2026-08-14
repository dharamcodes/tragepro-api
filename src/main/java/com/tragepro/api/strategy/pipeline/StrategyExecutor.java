package com.tragepro.api.strategy.pipeline;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

/** Pipeline step for executing strategy orders (buy, sell, notify). */
public interface StrategyExecutor {
  StrategyResponse execute(StrategyRequest strategyRequest);
}
