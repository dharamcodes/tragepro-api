package com.tragepro.api.strategy.execute;

import com.tragepro.api.strategy.model.request.WorkflowRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.copperengine.core.CopperException;
import org.copperengine.core.tranzient.TransientScottyEngine;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowExecutionService {

  private final TransientScottyEngine copperEngine;

  public void startEvaluatorWorkflow(WorkflowRequest request) {
    try {
      copperEngine.run("StrategyEvaluatorWorkflowImpl", request);
      log.info("Successfully submitted StrategyEvaluatorWorkflowImpl for request: {}", request);
    } catch (CopperException e) {
      log.error("Failed to start StrategyEvaluatorWorkflowImpl", e);
      throw new RuntimeException("Copper Engine submission failed", e);
    }
  }

  public void startExecutorWorkflow(WorkflowRequest request) {
    try {
      copperEngine.run("StrategyExecutorWorkflowImpl", request);
      log.info("Successfully submitted StrategyExecutorWorkflowImpl for request: {}", request);
    } catch (CopperException e) {
      log.error("Failed to start StrategyExecutorWorkflowImpl", e);
      throw new RuntimeException("Copper Engine submission failed", e);
    }
  }
}
