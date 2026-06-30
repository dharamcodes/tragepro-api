package com.tragepro.api.strategy.core.workflow.activity;

import io.temporal.activity.ActivityOptions;
import io.temporal.activity.LocalActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;

public interface BaseActivity {

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
