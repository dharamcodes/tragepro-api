package com.tragepro.api.strategy.core.workflow;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.workflow.activity.DataInitActivity;
import com.tragepro.api.strategy.core.workflow.impl.DataInitWorkflowImpl;
import io.temporal.workflow.Workflow;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class DataInitWorkflowImplTest {

  @Test
  void testExecute() {
    try (MockedStatic<Workflow> mockedWorkflow = mockStatic(Workflow.class)) {
      DataInitActivity mockLocalActivity = mock(DataInitActivity.class);
      DataInitActivity mockGlobalActivity = mock(DataInitActivity.class);

      mockedWorkflow
          .when(() -> Workflow.newLocalActivityStub(eq(DataInitActivity.class), any()))
          .thenReturn(mockLocalActivity);
      mockedWorkflow
          .when(() -> Workflow.newActivityStub(eq(DataInitActivity.class), any()))
          .thenReturn(mockGlobalActivity);

      DataInitWorkflowImpl workflow = new DataInitWorkflowImpl();

      StrategyRequest loadedConfig = StrategyRequest.builder().build();
      Set<StrategyRequest> loadedSymbol = Set.of(StrategyRequest.builder().build());
      Set<StrategyRequest> storedData = Set.of(StrategyRequest.builder().build());
      Set<StrategyResponse> expectedResponse = Set.of(StrategyResponse.builder().build());

      when(mockLocalActivity.loadConfig("StrategyOne")).thenReturn(loadedConfig);
      when(mockLocalActivity.loadSymbol(loadedConfig)).thenReturn(loadedSymbol);
      when(mockLocalActivity.storeData(loadedSymbol)).thenReturn(storedData);
      when(mockGlobalActivity.run(storedData)).thenReturn(expectedResponse);

      Set<StrategyResponse> result = workflow.execute("StrategyOne");
      assertNotNull(result);
      assertEquals(expectedResponse, result);
    }
  }
}
