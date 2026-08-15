package com.tragepro.api.strategy.core;

import com.tragepro.api.domain.strategy.constant.StrategyType;
import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;

public interface Strategy {
  StrategyResponse builder(StrategyRequest request);

  StrategyResponse evaluator(StrategyRequest request);

  StrategyResponse executor(StrategyRequest request);

  StrategyType getStrategyType();
}
