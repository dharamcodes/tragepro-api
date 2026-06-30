package com.tragepro.api.strategy.context;

import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyContextConfig {
  private final Map<SymbolData, StrategyResponse> strategyContext;

  public StrategyResponse get(SymbolData symbolData) {
    return strategyContext.get(symbolData);
  }

  public void put(SymbolData key, StrategyResponse value) {
    strategyContext.put(key, value);
  }
}
