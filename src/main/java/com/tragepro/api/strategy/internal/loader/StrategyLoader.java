package com.tragepro.api.strategy.internal.loader;

import com.tragepro.api.strategy.dto.StrategyResponse;

public interface StrategyLoader {
  StrategyResponse load(String strategyName);
}
