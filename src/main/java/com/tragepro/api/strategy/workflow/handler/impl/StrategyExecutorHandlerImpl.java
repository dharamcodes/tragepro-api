package com.tragepro.api.strategy.workflow.handler.impl;

import com.tragepro.api.strategy.model.request.WorkflowRequest;
import com.tragepro.api.strategy.workflow.handler.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StrategyExecutorHandlerImpl implements StrategyHandler {

  public void execute(WorkflowRequest request) {
    log.info("Executing strategy for request: {}", request);
    // TODO: Implement execution logic
  }
}
