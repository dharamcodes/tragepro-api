package com.tragepro.api.strategy.workflow;

import com.tragepro.api.strategy.model.request.WorkflowRequest;
import org.copperengine.core.Interrupt;
import org.copperengine.core.Workflow;
import org.copperengine.core.WorkflowDescription;

import com.tragepro.api.strategy.workflow.handler.impl.StrategyEvaluatorHandlerImpl;
import org.copperengine.core.AutoWire;

@WorkflowDescription(
    alias = "StrategyEvaluatorWorkflowImpl",
    majorVersion = 1,
    minorVersion = 0,
    patchLevelVersion = 0)
public class StrategyEvaluatorWorkflowImpl extends Workflow<WorkflowRequest>
    implements StrategyWorkflow {

  @AutoWire private transient StrategyEvaluatorHandlerImpl handler;

  @Override
  public void main() throws Interrupt {
    handler.evaluate(getData());
  }
}
