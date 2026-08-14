package com.tragepro.api.strategy.pipeline.executor;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.pipeline.StrategyExecutor;
import org.springframework.stereotype.Component;

@Component("EXECUTE_SELL")
public class ExecuteSell implements StrategyExecutor {

  @Override
  public StrategyResponse execute(StrategyRequest strategyRequest) {
    return null;
  }
}
