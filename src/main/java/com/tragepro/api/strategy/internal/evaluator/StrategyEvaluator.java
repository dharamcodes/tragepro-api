package com.tragepro.api.strategy.internal.evaluator;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;

public interface StrategyEvaluator {
  StrategyResponse evaluate(StrategyRequest strategyRequest);
}
