package com.tragepro.api.strategy.definition;

import com.tragepro.api.strategy.Strategy;
import com.tragepro.api.strategy.constant.StrategyType;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public class SwingStrategy implements Strategy {

  @Override
  public StrategyResponse builder(StrategyRequest request) {
    return null;
  }

  @Override
  public StrategyResponse evaluator(StrategyRequest request) {
    return null;
  }

  @Override
  public StrategyResponse executor(StrategyRequest request) {
    return null;
  }

  @Override
  public StrategyType getStrategyType() {
    return StrategyType.SWING_STRATEGY;
  }
}
