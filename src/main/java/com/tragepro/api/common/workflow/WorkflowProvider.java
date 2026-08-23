package com.tragepro.api.common.workflow;

import java.util.Collection;

public interface WorkflowProvider {
    Collection<Class<?>> getWorkflowImplementationTypes();
}
