package com.tragepro.api.strategy.execute;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.tragepro.api.strategy.model.request.WorkflowRequest;
import org.copperengine.core.CopperException;
import org.copperengine.core.tranzient.TransientScottyEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowExecutionServiceTest {

  @Mock private TransientScottyEngine copperEngine;

  @InjectMocks private WorkflowExecutionService workflowExecutionService;

  @Test
  void startEvaluatorWorkflow_success() throws Exception {
    WorkflowRequest request = WorkflowRequest.builder().strategyId("strat1").symbol("BTC").build();

    workflowExecutionService.startEvaluatorWorkflow(request);

    verify(copperEngine).run(eq("StrategyEvaluatorWorkflowImpl"), eq(request));
  }

  @Test
  void startEvaluatorWorkflow_exception() throws Exception {
    WorkflowRequest request = WorkflowRequest.builder().strategyId("strat1").symbol("BTC").build();

    doThrow(new CopperException("test"))
        .when(copperEngine)
        .run("StrategyEvaluatorWorkflowImpl", request);

    assertThrows(
        RuntimeException.class, () -> workflowExecutionService.startEvaluatorWorkflow(request));
  }

  @Test
  void startExecutorWorkflow_success() throws Exception {
    WorkflowRequest request = WorkflowRequest.builder().strategyId("strat1").symbol("BTC").build();

    workflowExecutionService.startExecutorWorkflow(request);

    verify(copperEngine).run(eq("StrategyExecutorWorkflowImpl"), eq(request));
  }

  @Test
  void startExecutorWorkflow_exception() throws Exception {
    WorkflowRequest request = WorkflowRequest.builder().strategyId("strat1").symbol("BTC").build();

    doThrow(new CopperException("test"))
        .when(copperEngine)
        .run("StrategyExecutorWorkflowImpl", request);

    assertThrows(
        RuntimeException.class, () -> workflowExecutionService.startExecutorWorkflow(request));
  }
}
