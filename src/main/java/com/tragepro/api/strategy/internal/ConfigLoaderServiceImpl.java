package com.tragepro.api.strategy.internal;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.strategy.ConfigLoaderService;
import com.tragepro.api.strategy.props.StrategyConfig;
import com.tragepro.api.strategy.props.WorkflowConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ConfigLoaderServiceImpl implements ConfigLoaderService {
  private final WorkflowConfig workflowConfig;

  @Override
  public StrategyConfig getStrategyByName(String name) {
    return workflowConfig.getStrategy().stream()
        .filter(s -> s.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElseThrow(() -> new AppException(ErrorType.INTERNAL_ERROR));
  }
}
