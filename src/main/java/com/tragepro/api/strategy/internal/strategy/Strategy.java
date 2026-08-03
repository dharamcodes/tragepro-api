package com.tragepro.api.strategy.internal.strategy;

import com.tragepro.api.strategy.StrategyType;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;

public interface Strategy {
  StrategyResponse builder(StrategyRequest request);

  StrategyResponse evaluator(StrategyRequest request);

  StrategyResponse executor(StrategyRequest request);

  StrategyType getStrategyType();
}
