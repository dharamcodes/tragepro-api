package com.tragepro.api.domain.strategy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyExecutorStep {
  EXECUTE_BUY("EXECUTE_BUY"),
  EXECUTE_SELL("EXECUTE_SELL"),
  EXECUTE_NOTIFY("EXECUTE_NOTIFY");

  private final String name;

  public static StrategyExecutorStep of(String value) {
    if (value == null) return null;
    return java.util.Arrays.stream(values())
        .filter(
            step -> step.name().equalsIgnoreCase(value) || step.getName().equalsIgnoreCase(value))
        .findFirst()
        .orElse(null);
  }
}
