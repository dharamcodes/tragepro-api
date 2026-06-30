package com.tragepro.api.strategy.core;

import com.tragepro.api.strategy.model.response.StrategyResponse;

public interface StrategyLoader {
  StrategyResponse load(String strategyName);
}
