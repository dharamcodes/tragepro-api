package com.tragepro.api.strategy.core.pipeline.builder;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.pipeline.StrategyBuilder;
import org.springframework.stereotype.Component;

@Component("BUILD_VWAP_LEVELS")
public class BuildVwapLevels implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest strategyRequest) {
    return null;
  }
}
