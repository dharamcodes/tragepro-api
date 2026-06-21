package com.tragepro.api.strategy.config;

import com.tragepro.api.strategy.model.StrategyStepsModel;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "workflow")
public class WorkflowProperties {
  private Set<StrategyStepsModel> strategySteps;
}
