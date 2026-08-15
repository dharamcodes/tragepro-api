package com.tragepro.api.strategy.core.pipeline;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;

/** Pipeline step for evaluating strategy signals (liquidity, volume, VWAP). */
public interface StrategyEvaluator {
  StrategyResponse evaluate(StrategyRequest strategyRequest);
}
