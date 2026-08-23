package com.tragepro.api.strategy.core.pipeline;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;

/** Pipeline step for executing strategy orders (buy, sell, notify). */
public interface StrategyExecutor {
    StrategyResponse execute(StrategyRequest strategyRequest);
}
