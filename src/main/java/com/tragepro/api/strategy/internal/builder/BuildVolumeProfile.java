package com.tragepro.api.strategy.internal.builder;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("BUILD_VOLUME_PROFILE")
public class BuildVolumeProfile implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest strategyRequest) {
    return null;
  }
}
