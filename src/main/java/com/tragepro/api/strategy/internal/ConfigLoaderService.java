package com.tragepro.api.strategy.internal;

import com.tragepro.api.strategy.props.StrategyConfig;

interface ConfigLoaderService {

  StrategyConfig getStrategyByName(String name);
}
