package com.tragepro.api.strategy.core.workflow.activity;

import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.constant.StrategyStep;
import com.tragepro.api.strategy.model.StatusModel;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import io.temporal.activity.ActivityOptions;
import io.temporal.activity.LocalActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;

public interface BaseActivity {

  static void evaluateState(Set<StrategyRequest> strategyRequests) {
    strategyRequests.forEach(
        response -> response.setCurrentState(nextStatus(response.getCurrentState())));
  }

  private static StatusModel nextStatus(StatusModel current) {
    return StatusModel.builder().state(nextState(current)).step(nextStep(current)).build();
  }

  private static StrategyState nextState(StatusModel current) {
    if (Objects.isNull(current)) {
      return StrategyState.INITIALIZING;
    }
    return StrategyState.nextState(current.getState()).orElse(StrategyState.INITIALIZING);
  }

  private static StrategyStep nextStep(StatusModel current) {
    if ((Objects.isNull(current))) {
      return StrategyStep.INIT;
    }
    return StrategyStep.nextStep(current.getStep()).orElse(StrategyStep.INIT);
  }

  static <C> C localActivity(Class<C> clazz) {
    return Workflow.newLocalActivityStub(
        clazz,
        LocalActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .setScheduleToCloseTimeout(Duration.ofMinutes(1))
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(3).build())
            .build());
  }

  static <C> C globalActivity(Class<C> clazz) {
    return Workflow.newActivityStub(
        clazz,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(Duration.ofSeconds(10))
            .setScheduleToCloseTimeout(Duration.ofMinutes(1))
            .setRetryOptions(RetryOptions.newBuilder().setMaximumAttempts(3).build())
            .build());
  }
}
