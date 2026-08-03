package com.tragepro.api.strategy.internal.strategy;

import com.tragepro.api.strategy.StrategyType;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;

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
