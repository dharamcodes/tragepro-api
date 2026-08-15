package com.tragepro.api.strategy.core.pipeline;

import com.tragepro.api.domain.strategy.response.StrategyResponse;

public interface StrategyLoader {
  StrategyResponse load(String strategyName);
}
