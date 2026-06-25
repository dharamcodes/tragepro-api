package com.tragepro.api.strategy.core;

import java.util.concurrent.TimeUnit;

public interface StrategyBuilder {
  void build(long time, TimeUnit uom);
}
