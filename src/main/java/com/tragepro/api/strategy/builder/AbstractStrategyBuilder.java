package com.tragepro.api.strategy.builder;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyBuilder {

  StrategyResponse build(StrategyRequest request);

  static StrategyResponse populateData(StrategyRequest strategyRequest) {
    return StrategyResponse.builder().build();
  }

  static StrategyResponse populateMeta(StrategyRequest strategyRequest) {
    return StrategyResponse.builder().build();
  }

  static StrategyResponse populateIndicator(StrategyRequest strategyRequest) {
    return StrategyResponse.builder().build();
  }
}
