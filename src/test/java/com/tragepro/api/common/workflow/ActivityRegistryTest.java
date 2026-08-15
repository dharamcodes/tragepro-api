package com.tragepro.api.common.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ActivityRegistryTest {

  static class DummyGlobalActivity extends BaseActivity {
    @Override
    public List<Class<?>> globalActivities() {
      return List.of(DummyGlobalActivity.class);
    }

    @Override
    public List<Class<?>> localActivities() {
      return List.of();
    }
  }

  @Test
  void testGlobalInstances() {
    DummyGlobalActivity activity = new DummyGlobalActivity();
    ActivityRegistry registry = new ActivityRegistry(List.of(activity));

    List<Object> instances = registry.globalInstances();
    assertEquals(1, instances.size());
    assertEquals(activity, instances.getFirst());
    assertEquals(1, registry.globalActivityInterfaces().size());
    assertEquals(0, registry.localInstances().size());
  }
}
