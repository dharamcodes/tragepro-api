package com.tragepro.api.strategy.core.props;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepsConfig {
    private List<BuilderConfig> strategyBuilder;
    private List<EvaluatorConfig> strategyEvaluator;
    private List<ExecutorConfig> strategyExecutor;
}
