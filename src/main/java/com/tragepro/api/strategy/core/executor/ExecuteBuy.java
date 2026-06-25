package com.tragepro.api.strategy.core.executor;

import com.tragepro.api.strategy.core.StrategyExecutor;
import org.springframework.stereotype.Component;

@Component("EXECUTE_BUY")
public class ExecuteBuy implements StrategyExecutor {
  @Override
  public void execute() {
    System.out.println("Executing buy order");
  }
}
