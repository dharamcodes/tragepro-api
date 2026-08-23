package com.tragepro.api.strategy.adapter.impl;

import com.tragepro.api.strategy.adapter.ConfigLoaderAdapter;
import com.tragepro.api.strategy.core.props.StrategyConfig;
import com.tragepro.api.strategy.service.ConfigLoaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigLoaderAdapterImpl implements ConfigLoaderAdapter {
    private final ConfigLoaderService configLoaderService;

    @Override
    public StrategyConfig getStrategyByName(String name) {
        return configLoaderService.getStrategyByName(name);
    }
}
