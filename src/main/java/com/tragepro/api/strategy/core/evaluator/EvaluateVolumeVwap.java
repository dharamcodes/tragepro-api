package com.tragepro.api.strategy.core.evaluator;

import com.tragepro.api.strategy.core.StrategyEvaluator;
import org.springframework.stereotype.Component;

@Component("EVALUATE_VOLUME_VWAP")
public class EvaluateVolumeVwap implements StrategyEvaluator {
  @Override
  public void evaluate(double probability) {
    System.out.println("Evaluating volume VWAP with probability " + probability);
  }
}
