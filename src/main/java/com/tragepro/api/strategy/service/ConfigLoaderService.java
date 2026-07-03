package com.tragepro.api.strategy.service;

import com.tragepro.api.strategy.props.StrategyConfig;

public interface ConfigLoaderService {

  StrategyConfig getStrategyByName(String name);
}
