package com.tragepro.api.strategy.props;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "workflow")
public class WorkflowConfig {
  private List<StrategyConfig> strategy;

  public StrategyConfig getStrategyByName(String name) {
    return strategy.stream()
        .filter(cfg -> cfg.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Strategy not found: " + name));
  }
}
