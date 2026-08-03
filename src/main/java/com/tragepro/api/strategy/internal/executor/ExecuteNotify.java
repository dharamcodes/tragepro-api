package com.tragepro.api.strategy.internal.executor;

import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("EXECUTE_NOTIFY")
public class ExecuteNotify implements StrategyExecutor {

  @Override
  public StrategyResponse execute(StrategyRequest strategyRequest) {
    return null;
  }
}
