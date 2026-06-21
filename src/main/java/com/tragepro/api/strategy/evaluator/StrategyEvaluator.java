package com.tragepro.api.strategy.evaluator;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyEvaluator {
  StrategyResponse evaluate(StrategyRequest request);
}
