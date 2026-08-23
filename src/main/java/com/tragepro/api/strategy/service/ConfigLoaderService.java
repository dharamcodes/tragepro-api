package com.tragepro.api.strategy.service;

import com.tragepro.api.strategy.core.props.StrategyConfig;

public interface ConfigLoaderService {

    StrategyConfig getStrategyByName(String name);
}
