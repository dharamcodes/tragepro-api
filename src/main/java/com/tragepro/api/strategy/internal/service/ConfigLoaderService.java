package com.tragepro.api.strategy.internal.service;

import com.tragepro.api.strategy.internal.props.StrategyConfig;

/** Service interface for loading strategy configurations from application properties. */
public interface ConfigLoaderService {

  /**
   * Loads a strategy configuration by strategy name.
   *
   * @param name strategy name
   * @return strategy configuration
   */
  StrategyConfig getStrategyByName(String name);
}
