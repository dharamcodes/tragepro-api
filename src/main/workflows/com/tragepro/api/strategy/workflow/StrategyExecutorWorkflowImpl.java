package com.tragepro.api.strategy.workflow;

import com.tragepro.api.strategy.model.request.WorkflowRequest;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Workflow;
import org.copperengine.core.WorkflowDescription;

import com.tragepro.api.strategy.workflow.handler.impl.StrategyExecutorHandlerImpl;
import org.copperengine.core.AutoWire;

@WorkflowDescription(
    alias = "StrategyExecutorWorkflowImpl",
    majorVersion = 1,
    minorVersion = 0,
    patchLevelVersion = 0)
public class StrategyExecutorWorkflowImpl extends Workflow<WorkflowRequest>
    implements StrategyWorkflow {

  @AutoWire private transient StrategyExecutorHandlerImpl handler;

  @Override
  public void main() throws Interrupt {
    handler.execute(getData());
  }
}
