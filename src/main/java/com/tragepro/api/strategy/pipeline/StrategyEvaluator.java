package com.tragepro.api.strategy.pipeline;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

/** Pipeline step for evaluating strategy signals (liquidity, volume, VWAP). */
public interface StrategyEvaluator {
  StrategyResponse evaluate(StrategyRequest strategyRequest);
}
