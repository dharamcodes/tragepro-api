package com.tragepro.api.strategy.pipeline;

import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;

/** Pipeline step for building strategy data (indicators, levels, OHLCV timeframe aggregation). */
public interface StrategyBuilder {
  StrategyResponse build(StrategyRequest strategyRequest);
}
