package com.tragepro.api.strategy.builder;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public abstract class AbstractStrategyBuilder {

  abstract StrategyResponse build();

  public static StrategyResponse populateData(
      StrategyRequest.StrategyRequestBuilder requestBuilder) {
    return StrategyResponse.builder().build();
  }

  public static StrategyResponse populateMeta(
      StrategyRequest.StrategyRequestBuilder requestBuilder) {
    return StrategyResponse.builder().build();
  }

  public static StrategyResponse populateIndicator(
      StrategyRequest.StrategyRequestBuilder requestBuilder) {
    return StrategyResponse.builder().build();
  }
}
