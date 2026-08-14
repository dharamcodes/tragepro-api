package com.tragepro.api.common.config;

import com.tragepro.api.common.properties.TemporalProperties;
import com.tragepro.api.common.workflow.ActivityRegistry;
import com.tragepro.api.common.workflow.WorkflowRegistry;
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
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(
    prefix = "temporal",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = false)
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
      WorkerFactory factory,
      TemporalProperties props,
      ActivityRegistry activityRegistry,
      WorkflowRegistry workflowRegistry) {
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

    Class<?>[] workflowClasses =
        workflowRegistry.workflowImplementationTypes().toArray(new Class<?>[0]);
    if (workflowClasses.length > 0) {
      worker.registerWorkflowImplementationTypes(options, workflowClasses);
    }
    worker.registerActivitiesImplementations(activityRegistry.globalInstances().toArray());
    factory.start();
    return worker;
  }
}
