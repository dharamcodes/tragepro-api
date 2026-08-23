package com.tragepro.api.strategy.core.workflow;

import com.tragepro.api.common.workflow.WorkflowProvider;
import com.tragepro.api.strategy.core.workflow.impl.DataInitWorkflowImpl;
import java.util.Collection;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StrategyWorkflowProvider implements WorkflowProvider {

    @Override
    public Collection<Class<?>> getWorkflowImplementationTypes() {
        return List.of(DataInitWorkflowImpl.class);
    }
}
