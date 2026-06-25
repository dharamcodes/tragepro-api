package com.tragepro.api.strategy.core.evaluator;

import com.tragepro.api.strategy.core.StrategyEvaluator;
import org.springframework.stereotype.Component;

@Component("EVALUATE_VOLUME_PROFILE")
public class EvaluateVolumeProfile implements StrategyEvaluator {
  @Override
  public void evaluate(double probability) {
    System.out.println("Evaluating volume profile with probability " + probability);
  }
}
