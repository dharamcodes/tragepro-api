package com.tragepro.api.strategy;

import com.tragepro.api.common.constant.StrategyType;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public class IntradayStrategy implements Strategy {

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
    return StrategyType.INTRADAY_STRATEGY;
  }
}
