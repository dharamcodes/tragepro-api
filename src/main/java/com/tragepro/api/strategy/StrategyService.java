package com.tragepro.api.strategy;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.Set;

public interface StrategyService {
  StrategyResponse create(StrategyRequest strategyRequest);

  StrategyResponse createOrUpdate(StrategyRequest strategyRequest);

  Set<StrategyResponse> getAll();
}
