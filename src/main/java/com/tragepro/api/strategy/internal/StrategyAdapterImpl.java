package com.tragepro.api.strategy.internal;

import com.tragepro.api.strategy.StrategyAdapter;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class StrategyAdapterImpl implements StrategyAdapter {

  private final StrategyService strategyService;

  @Override
  public StrategyResponse runStrategy(StrategyRequest request) {
    return strategyService.createOrUpdate(request);
  }
}
