package com.tragepro.api.strategy.adapter;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import java.util.Set;

public interface StrategyAdapter {
  StrategyResponse create(StrategyRequest strategyRequest);

  StrategyResponse createOrUpdate(StrategyRequest strategyRequest);

  Set<StrategyResponse> getAll();
}
