package com.tragepro.api.strategy.pipeline;

import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyLoader {
  StrategyResponse load(String strategyName);
}
