package com.tragepro.api.strategy.internal.evaluator;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("EVALUATE_LIQUIDITY_LEVELS")
public class EvaluateLiquidityLevels implements StrategyEvaluator {

  @Override
  public StrategyResponse evaluate(StrategyRequest strategyRequest) {
    return null;
  }
}
