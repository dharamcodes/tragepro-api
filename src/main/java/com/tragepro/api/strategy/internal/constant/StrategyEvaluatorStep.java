package com.tragepro.api.strategy.internal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyEvaluatorStep {
  EVALUATE_VOLUME_PROFILE("EVALUATE_VOLUME_PROFILE"),
  EVALUATE_VOLUME_VWAP("EVALUATE_VOLUME_VWAP"),
  EVALUATE_LIQUIDITY_LEVELS("EVALUATE_LIQUIDITY_LEVELS");

  private final String name;

  public static StrategyEvaluatorStep of(String value) {
    if (value == null) return null;
    return java.util.Arrays.stream(values())
        .filter(
            step -> step.name().equalsIgnoreCase(value) || step.getName().equalsIgnoreCase(value))
        .findFirst()
        .orElse(null);
  }
}
