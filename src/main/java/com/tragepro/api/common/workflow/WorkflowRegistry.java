package com.tragepro.api.common.workflow;

import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkflowRegistry {

  private final List<WorkflowProvider> workflowProviders;

  public Collection<Class<?>> workflowImplementationTypes() {
    return workflowProviders.stream()
        .flatMap(provider -> provider.getWorkflowImplementationTypes().stream())
        .toList();
  }
}
