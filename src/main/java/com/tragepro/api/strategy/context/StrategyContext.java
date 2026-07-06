package com.tragepro.api.strategy.context;

import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyContext {
  private final Map<String, StrategyResponse> strategyContext = new HashMap<>();

  public StrategyResponse get(String strategyName) {
    return strategyContext.get(strategyName);
  }

  public void put(String strategyName, StrategyResponse strategyResponse) {
    strategyContext.put(strategyName, strategyResponse);
  }
}
