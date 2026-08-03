package com.tragepro.api.strategy.internal.builder;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("BUILD_TIMEFRAME_DATA")
public class BuildTimeframeData implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest strategyRequest) {
    return null;
  }
}
