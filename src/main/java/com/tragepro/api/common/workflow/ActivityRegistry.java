package com.tragepro.api.common.workflow;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Registry that collects all {@link BaseActivity} beans and exposes them for Temporal worker
 * registration. Activities are categorized into global (remote) and local based on each
 * implementation's declared interfaces.
 */
@Component
@RequiredArgsConstructor
public class ActivityRegistry {

  private final List<BaseActivity> activities;

  /** Returns all activity bean instances that declare global activity interfaces. */
  public List<Object> globalInstances() {
    return activities.stream()
        .filter(a -> !a.globalActivities().isEmpty())
        .map(Object.class::cast)
        .toList();
  }

  /** Returns all activity bean instances that declare local activity interfaces. */
  public List<Object> localInstances() {
    return activities.stream()
        .filter(a -> !a.localActivities().isEmpty())
        .map(Object.class::cast)
        .toList();
  }

  /** Returns a flat list of all global activity interface classes for worker registration. */
  public Collection<Class<?>> globalActivityInterfaces() {
    return activities.stream().flatMap(a -> a.globalActivities().stream()).toList();
  }
}
