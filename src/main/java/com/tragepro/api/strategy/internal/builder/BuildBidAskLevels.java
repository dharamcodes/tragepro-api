package com.tragepro.api.strategy.internal.builder;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("BUILD_BID_ASK_LEVELS")
public class BuildBidAskLevels implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest strategyRequest) {
    return null;
  }
}
