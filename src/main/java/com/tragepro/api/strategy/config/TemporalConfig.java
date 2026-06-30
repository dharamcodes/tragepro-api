package com.tragepro.api.strategy.config;

import com.tragepro.api.common.props.TemporalProperties;
import com.tragepro.api.strategy.core.workflow.activity.BaseActivity;
import com.tragepro.api.strategy.core.workflow.impl.DataInitWorkflowImpl;
import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.worker.WorkflowImplementationOptions;
import java.time.Duration;
import java.util.Map;
import java.util.Set;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "temporal.server", name = "target")
public class TemporalConfig {

  @Bean
  public WorkflowServiceStubs workflowService(TemporalProperties props) {
    return WorkflowServiceStubs.newServiceStubs(
        WorkflowServiceStubsOptions.newBuilder().setTarget(props.getServer().getTarget()).build());
  }

  @Bean
  public WorkflowClient workflowClient(
      WorkflowServiceStubs workflowService, TemporalProperties props) {
    return WorkflowClient.newInstance(
        workflowService,
        WorkflowClientOptions.newBuilder()
            .setNamespace(props.getServer().getNamespace())
            .setIdentity(props.getClient().getIdentity())
            .build());
  }

  @Bean
  public WorkerFactory workerFactory(WorkflowClient client) {
    return WorkerFactory.newInstance(client);
  }

  @Bean
  public Worker worker(
      WorkerFactory factory, TemporalProperties props, Set<BaseActivity> strategyActivities) {
    Worker worker = factory.newWorker(props.getWorker().getTaskQueue());

    ActivityOptions activityOptions =
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(
                Duration.ofSeconds(props.getWorker().getActivityStartToCloseTimeoutSeconds()))
            .build();

    WorkflowImplementationOptions options =
        WorkflowImplementationOptions.newBuilder()
            .setActivityOptions(Map.of("DataInitActivity", activityOptions))
            .build();

    worker.registerWorkflowImplementationTypes(options, DataInitWorkflowImpl.class);
    worker.registerActivitiesImplementations(strategyActivities.toArray());
    factory.start();
    return worker;
  }
}
