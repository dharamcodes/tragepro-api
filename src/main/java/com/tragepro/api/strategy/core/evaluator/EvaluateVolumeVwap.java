package com.tragepro.api.strategy.core.evaluator;

import com.tragepro.api.strategy.core.StrategyEvaluator;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("EVALUATE_VOLUME_VWAP")
public class EvaluateVolumeVwap implements StrategyEvaluator {

  @Override
  public StrategyResponse evaluate(StrategyRequest strategyRequest) {
    return null;
  }
}
