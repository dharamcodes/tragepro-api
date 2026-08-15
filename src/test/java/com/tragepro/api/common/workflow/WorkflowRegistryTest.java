package com.tragepro.api.common.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WorkflowRegistryTest {

  @Mock private WorkflowProvider provider;

  @Test
  void testWorkflowImplementationTypes() {
    when(provider.getWorkflowImplementationTypes()).thenReturn(List.of(String.class));
    WorkflowRegistry registry = new WorkflowRegistry(List.of(provider));

    Collection<Class<?>> types = registry.workflowImplementationTypes();
    assertNotNull(types);
    assertEquals(1, types.size());
    assertEquals(String.class, types.iterator().next());
  }
}
