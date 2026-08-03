package com.tragepro.api.strategy.internal.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyBuilderStep {
  BUILD_TIMEFRAME_DATA("BUILD_TIMEFRAME_DATA"),
  BUILD_VOLUME_PROFILE("BUILD_VOLUME_PROFILE"),
  BUILD_BID_ASK_LEVELS("BUILD_BID_ASK_LEVELS"),
  BUILD_VWAP_LEVELS("BUILD_VWAP_LEVELS"),
  BUILD_LIQUIDITY_LEVELS("BUILD_LIQUIDITY_LEVELS");

  private final String name;

  public static StrategyBuilderStep of(String value) {
    if (value == null) return null;
    return java.util.Arrays.stream(values())
        .filter(
            step -> step.name().equalsIgnoreCase(value) || step.getName().equalsIgnoreCase(value))
        .findFirst()
        .orElse(null);
  }
}
