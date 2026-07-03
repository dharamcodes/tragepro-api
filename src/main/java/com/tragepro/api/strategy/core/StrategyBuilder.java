package com.tragepro.api.strategy.core;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyBuilder {
  StrategyResponse build(StrategyRequest strategyRequest);
}
