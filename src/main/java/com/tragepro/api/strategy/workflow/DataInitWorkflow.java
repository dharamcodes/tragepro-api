package com.tragepro.api.strategy.workflow;

import com.tragepro.api.common.workflow.BaseWorkflow;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import java.util.Set;

@WorkflowInterface
public interface DataInitWorkflow extends BaseWorkflow {

  @WorkflowMethod
  Set<StrategyResponse> execute(String strategyName);
}
