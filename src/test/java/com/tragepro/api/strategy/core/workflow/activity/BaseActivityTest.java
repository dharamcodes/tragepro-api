package com.tragepro.api.strategy.core.workflow.activity;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.constant.StrategyStep;
import com.tragepro.api.strategy.model.StatusModel;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BaseActivityTest {

  @Test
  void testEvaluateState() {
    StrategyRequest request =
        StrategyRequest.builder()
            .currentState(
                StatusModel.builder()
                    .state(StrategyState.INITIALIZING)
                    .step(StrategyStep.INIT)
                    .build())
            .build();

    BaseActivity.evaluateState(Set.of(request));
    assertNotNull(request.getCurrentState());
    assertEquals(StrategyState.BUILDING, request.getCurrentState().getState());
    assertEquals(StrategyStep.BUILD, request.getCurrentState().getStep());
  }

  @Test
  void testEvaluateState_NullCurrentState() {
    StrategyRequest request = StrategyRequest.builder().build();
    BaseActivity.evaluateState(Set.of(request));
    assertNotNull(request.getCurrentState());
    assertEquals(StrategyState.INITIALIZING, request.getCurrentState().getState());
    assertEquals(StrategyStep.INIT, request.getCurrentState().getStep());
  }

  @Test
  void testLocalActivityStub_ThrowsExceptionOutsideWorkflowThread() {
    assertThrows(Error.class, () -> BaseActivity.localActivity(DataInitActivity.class));
  }

  @Test
  void testGlobalActivityStub_ThrowsExceptionOutsideWorkflowThread() {
    assertThrows(Error.class, () -> BaseActivity.globalActivity(DataInitActivity.class));
  }
}
