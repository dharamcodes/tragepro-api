package com.tragepro.api.strategy.scheduler;

import com.tragepro.api.common.props.TemporalProperties;
import com.tragepro.api.strategy.core.workflow.DataInitWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(WorkflowClient.class)
public class StrategyWorkflowExecutor {

  private final WorkflowClient workflowClient;
  private final TemporalProperties temporalProperties;

  @Scheduled(initialDelay = 10000, fixedRate = 500000000)
  public void schedule() {
    log.info("Starting scheduled DataInitWorkflow...");
    DataInitWorkflow workflow =
        workflowClient.newWorkflowStub(
            DataInitWorkflow.class,
            WorkflowOptions.newBuilder()
                .setWorkflowId("strategy-workflow-" + System.currentTimeMillis())
                .setTaskQueue(temporalProperties.getWorker().getTaskQueue())
                .build());
    WorkflowClient.start(workflow::execute, "INTRADAY_VP_VWAP");
    log.info("DataInitWorkflow started successfully.");
  }
}
