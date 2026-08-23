package com.tragepro.api.strategy.adapter.impl;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.adapter.StrategyAdapter;
import com.tragepro.api.strategy.service.StrategyService;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyAdapterImpl implements StrategyAdapter {
    private final StrategyService strategyService;

    @Override
    public StrategyResponse create(StrategyRequest strategyRequest) {
        return strategyService.create(strategyRequest);
    }

    @Override
    public StrategyResponse createOrUpdate(StrategyRequest strategyRequest) {
        return strategyService.createOrUpdate(strategyRequest);
    }

    @Override
    public Set<StrategyResponse> getAll() {
        return strategyService.getAll();
    }
}
