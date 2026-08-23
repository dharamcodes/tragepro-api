package com.tragepro.api.strategy.core.pipeline;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;

/** Pipeline step for building strategy data (indicators, levels, OHLCV timeframe aggregation). */
public interface StrategyBuilder {
    StrategyResponse build(StrategyRequest strategyRequest);
}
