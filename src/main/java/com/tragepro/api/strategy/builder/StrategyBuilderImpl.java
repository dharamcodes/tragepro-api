package com.tragepro.api.strategy.builder;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyBuilderImpl implements StrategyBuilder {

  @Override
  public StrategyResponse build(StrategyRequest request) {
    return null;
  }

  public StrategyBuilderImpl name(String name) {
    return this;
  }
}
