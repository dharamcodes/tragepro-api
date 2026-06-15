package com.tragepro.api.strategy.core.impl;

import com.tragepro.api.strategy.core.StrategyWorkflow;
import com.tragepro.api.strategy.model.request.StrategyWorkflowRequest;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Workflow;
import org.copperengine.core.WorkflowDescription;

@WorkflowDescription(
    alias = "StrategyEvaluatorWorkflowImpl",
    majorVersion = 1,
    minorVersion = 0,
    patchLevelVersion = 0)
public class StrategyEvaluatorWorkflowImpl extends Workflow<StrategyWorkflowRequest>
    implements StrategyWorkflow {

  @Override
  public void main() throws Interrupt {}
}
