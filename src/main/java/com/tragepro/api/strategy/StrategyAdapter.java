package com.tragepro.api.strategy;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyAdapter {

  StrategyResponse runStrategy(StrategyRequest request);
}
