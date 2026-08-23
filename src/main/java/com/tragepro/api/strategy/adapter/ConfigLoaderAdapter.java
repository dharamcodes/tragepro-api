package com.tragepro.api.strategy.adapter;

import com.tragepro.api.strategy.core.props.StrategyConfig;

public interface ConfigLoaderAdapter {
    StrategyConfig getStrategyByName(String name);
}
