package com.tragepro.api.strategy.internal.builder;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;

public interface StrategyBuilder {
  StrategyResponse build(StrategyRequest strategyRequest);
}
