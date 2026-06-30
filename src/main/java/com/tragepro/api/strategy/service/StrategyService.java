package com.tragepro.api.strategy.service;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyService {
  StrategyResponse create(StrategyRequest strategyRequest);

  StrategyResponse createOrUpdate(StrategyRequest strategyRequest);
}
