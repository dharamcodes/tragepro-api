package com.tragepro.api.strategy.service;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import java.util.Set;

public interface StrategyService {
  StrategyResponse create(StrategyRequest strategyRequest);

  StrategyResponse createOrUpdate(StrategyRequest strategyRequest);

  Set<StrategyResponse> getAll();
}
