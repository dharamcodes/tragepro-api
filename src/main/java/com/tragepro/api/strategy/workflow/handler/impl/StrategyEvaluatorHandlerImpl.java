package com.tragepro.api.strategy.workflow.handler.impl;

import com.tragepro.api.strategy.model.request.WorkflowRequest;
import com.tragepro.api.strategy.workflow.handler.StrategyHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StrategyEvaluatorHandlerImpl implements StrategyHandler {

  public void evaluate(WorkflowRequest request) {
    log.info("Evaluating strategy for request: {}", request);
    // TODO: Implement evaluation logic
  }
}
