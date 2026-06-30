package com.tragepro.api.strategy.core.evaluator;

import com.tragepro.api.strategy.core.StrategyEvaluator;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("EVALUATE_LIQUIDITY_LEVELS")
public class EvaluateLiquidityLevels implements StrategyEvaluator {

  @Override
  public StrategyResponse evaluate(StrategyRequest strategyRequest) {
    return null;
  }
}
