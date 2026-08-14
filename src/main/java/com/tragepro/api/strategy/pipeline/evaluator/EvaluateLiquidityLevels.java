package com.tragepro.api.strategy.pipeline.evaluator;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.pipeline.StrategyEvaluator;
import org.springframework.stereotype.Component;

@Component("EVALUATE_LIQUIDITY_LEVELS")
public class EvaluateLiquidityLevels implements StrategyEvaluator {

  @Override
  public StrategyResponse evaluate(StrategyRequest strategyRequest) {
    return null;
  }
}
