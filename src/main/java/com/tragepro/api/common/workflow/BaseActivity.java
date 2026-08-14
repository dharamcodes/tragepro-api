package com.tragepro.api.common.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.activity.LocalActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.workflow.Workflow;
import java.time.Duration;
import java.util.List;

/**
 * Base contract for all Temporal activity implementations in the system.
 *
 * <p>Subclasses must declare which activity interfaces they expose via {@link #globalActivities()}
 * and {@link #localActivities()}. These are consumed by {@link ActivityRegistry} to auto-register
 * activities on Temporal workers — no manual wiring needed.
 *
 * <p>Factory methods {@link #globalActivity(Class)} and {@link #localActivity(Class)} provide
 * pre-configured stubs usable inside Workflow implementations (Template Method pattern).
 */
public abstract class BaseActivity {

  private static final Duration START_TO_CLOSE = Duration.ofSeconds(10);
  private static final Duration SCHEDULE_TO_CLOSE = Duration.ofMinutes(1);
  private static final int MAX_RETRY_ATTEMPTS = 3;

  /**
   * Creates a Temporal global activity stub with standard retry and timeout options.
   *
   * @param clazz the {@code @ActivityInterface} type
   * @param <C> the activity interface type
   * @return activity stub for use inside a Temporal Workflow
   */
  public static <C> C globalActivity(Class<C> clazz) {
    return Workflow.newActivityStub(
        clazz,
        ActivityOptions.newBuilder()
            .setStartToCloseTimeout(START_TO_CLOSE)
            .setScheduleToCloseTimeout(SCHEDULE_TO_CLOSE)
            .setRetryOptions(
                RetryOptions.newBuilder().setMaximumAttempts(MAX_RETRY_ATTEMPTS).build())
            .build());
  }

  /**
   * Creates a Temporal local activity stub with standard retry and timeout options.
   *
   * @param clazz the {@code @ActivityInterface} type
   * @param <C> the activity interface type
   * @return local activity stub for use inside a Temporal Workflow
   */
  public static <C> C localActivity(Class<C> clazz) {
    return Workflow.newLocalActivityStub(
        clazz,
        LocalActivityOptions.newBuilder()
            .setStartToCloseTimeout(START_TO_CLOSE)
            .setScheduleToCloseTimeout(SCHEDULE_TO_CLOSE)
            .setRetryOptions(
                RetryOptions.newBuilder().setMaximumAttempts(MAX_RETRY_ATTEMPTS).build())
            .build());
  }

  /**
   * Returns the list of global {@code @ActivityInterface} classes this bean implements.
   *
   * @return list of global activity interface classes, empty if none
   */
  public abstract List<Class<?>> globalActivities();

  /**
   * Returns the list of local {@code @ActivityInterface} classes this bean implements.
   *
   * @return list of local activity interface classes, empty if none
   */
  public abstract List<Class<?>> localActivities();
}
