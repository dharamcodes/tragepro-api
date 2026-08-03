package com.tragepro.api.strategy.internal.evaluator;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("EVALUATE_VOLUME_PROFILE")
public class EvaluateVolumeProfile implements StrategyEvaluator {

  @Override
  public StrategyResponse evaluate(StrategyRequest strategyRequest) {
    return null;
  }
}
