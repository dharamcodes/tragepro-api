package com.tragepro.api.strategy.constant;

import com.tragepro.api.strategy.builder.AbstractStrategyBuilder;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.function.Function;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BuilderSteps {
  POPULATE_DATA(AbstractStrategyBuilder::populateData),
  POPULATE_META(AbstractStrategyBuilder::populateMeta),
  POPULATE_INDICATOR(AbstractStrategyBuilder::populateIndicator);

  private final Function<StrategyRequest.StrategyRequestBuilder, StrategyResponse> function;

  public StrategyResponse call(StrategyRequest.StrategyRequestBuilder request) {
    return function.apply(request);
  }
}
