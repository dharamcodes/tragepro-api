package com.tragepro.api.strategy.core.pipeline.evaluator;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.pipeline.StrategyEvaluator;
import org.springframework.stereotype.Component;

@Component("EVALUATE_VOLUME_VWAP")
public class EvaluateVolumeVwap implements StrategyEvaluator {

  @Override
  public StrategyResponse evaluate(StrategyRequest strategyRequest) {
    return null;
  }
}
