package com.tragepro.api.strategy.internal.workflow;

import com.tragepro.api.strategy.dto.StrategyResponse;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.Set;

@WorkflowInterface
public interface DataInitWorkflow {

  @WorkflowMethod
  Set<StrategyResponse> execute(String strategyName);
}
