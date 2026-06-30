package com.tragepro.api.strategy.core.executor;

import com.tragepro.api.strategy.core.StrategyExecutor;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.springframework.stereotype.Component;

@Component("EXECUTE_NOTIFY")
public class ExecuteNotify implements StrategyExecutor {

  @Override
  public StrategyResponse execute(StrategyRequest strategyRequest) {
    return null;
  }
}
