package com.tragepro.api.strategy;

import com.tragepro.api.common.constant.StrategyType;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface Strategy {
  StrategyResponse builder(StrategyRequest request);

  StrategyResponse evaluator(StrategyRequest request);

  StrategyResponse executor(StrategyRequest request);

  StrategyType getStrategyType();
}
