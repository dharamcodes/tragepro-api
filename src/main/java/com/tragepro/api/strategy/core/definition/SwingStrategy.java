package com.tragepro.api.strategy.core.definition;

import com.tragepro.api.domain.strategy.constant.StrategyType;
import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.Strategy;

public class SwingStrategy implements Strategy {

    @Override
    public StrategyResponse builder(StrategyRequest request) {
        return null;
    }

    @Override
    public StrategyResponse evaluator(StrategyRequest request) {
        return null;
    }

    @Override
    public StrategyResponse executor(StrategyRequest request) {
        return null;
    }

    @Override
    public StrategyType getStrategyType() {
        return StrategyType.SWING_STRATEGY;
    }
}
