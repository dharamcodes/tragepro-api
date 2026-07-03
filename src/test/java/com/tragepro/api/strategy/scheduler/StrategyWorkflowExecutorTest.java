package com.tragepro.api.strategy.scheduler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.props.TemporalProperties;
import com.tragepro.api.common.props.WorkerProperties;
import com.tragepro.api.strategy.core.workflow.DataInitWorkflow;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.workflow.Functions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyWorkflowExecutorTest {

  @Mock private WorkflowClient workflowClient;
  @Mock private TemporalProperties temporalProperties;
  @Mock private DataInitWorkflow workflowStub;

  @InjectMocks private StrategyWorkflowExecutor executor;

  @Test
  void testSchedule() {
    WorkerProperties worker = new WorkerProperties();
    worker.setTaskQueue("test-queue");
    when(temporalProperties.getWorker()).thenReturn(worker);

    when(workflowClient.newWorkflowStub(eq(DataInitWorkflow.class), any(WorkflowOptions.class)))
        .thenReturn(workflowStub);

    try (MockedStatic<WorkflowClient> mockedStatic = mockStatic(WorkflowClient.class)) {
      executor.schedule();
      mockedStatic.verify(
          () -> WorkflowClient.start(any(Functions.Func1.class), anyString()), times(1));
    }
  }
}
