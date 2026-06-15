package com.tragepro.api.strategy.core.impl;

import com.tragepro.api.strategy.core.StrategyWorkflow;
import com.tragepro.api.strategy.model.request.StrategyWorkflowRequest;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Workflow;
import org.copperengine.core.WorkflowDescription;

@WorkflowDescription(
    alias = "StrategyExecutorWorkflowImpl",
    majorVersion = 1,
    minorVersion = 0,
    patchLevelVersion = 0)
public class StrategyExecutorWorkflowImpl extends Workflow<StrategyWorkflowRequest>
    implements StrategyWorkflow {

  @Override
  public void main() throws Interrupt {}
}
