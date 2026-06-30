package com.tragepro.api.strategy.core.builder;

import com.tragepro.api.strategy.core.StrategyBuilder;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("BUILD_VWAP_LEVELS")
public class BuildVwapLevels implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest strategyRequest) {
    return null;
  }
}
