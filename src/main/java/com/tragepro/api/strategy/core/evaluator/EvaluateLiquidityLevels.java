package com.tragepro.api.strategy.core.evaluator;

import com.tragepro.api.strategy.core.StrategyEvaluator;
import org.springframework.stereotype.Component;

@Component("EVALUATE_LIQUIDITY_LEVELS")
public class EvaluateLiquidityLevels implements StrategyEvaluator {
  @Override
  public void evaluate(double probability) {
    System.out.println("Evaluating liquidity levels with probability " + probability);
  }
}
