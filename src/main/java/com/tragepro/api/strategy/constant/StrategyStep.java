package com.tragepro.api.strategy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyStep {
  INIT("INIT", 0),
  BUILD("BUILD", 1),
  EVALUATE("EVALUATE", 3),
  EXECUTE("EXECUTE", 4);

  private final String name;
  private final int priority;
}
