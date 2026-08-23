package com.tragepro.api.strategy.core.context;

import com.tragepro.api.domain.strategy.response.StrategyResponse;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyContext {
    private final Map<String, StrategyResponse> strategyContext = new ConcurrentHashMap<>();

    public StrategyResponse get(String strategyName) {
        return strategyContext.get(strategyName);
    }

    public void put(String strategyName, StrategyResponse strategyResponse) {
        strategyContext.put(strategyName, strategyResponse);
    }
}
