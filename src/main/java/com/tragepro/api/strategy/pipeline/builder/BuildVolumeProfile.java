package com.tragepro.api.strategy.pipeline.builder;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.pipeline.StrategyBuilder;
import org.springframework.stereotype.Component;

@Component("BUILD_VOLUME_PROFILE")
public class BuildVolumeProfile implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest strategyRequest) {
    return null;
  }
}
