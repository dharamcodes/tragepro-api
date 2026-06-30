package com.tragepro.api.strategy.service.impl;

import com.tragepro.api.strategy.props.StrategyConfig;
import com.tragepro.api.strategy.props.WorkflowConfig;
import com.tragepro.api.strategy.service.ConfigLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfigLoaderServiceImpl implements ConfigLoaderService {
  private final WorkflowConfig workflowConfig;

  @Override
  public StrategyConfig getStrategyByName(String name) {
    return workflowConfig.getStrategy().stream()
        .filter(s -> s.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Strategy not found: " + name));
  }
}
