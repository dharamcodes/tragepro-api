package com.tragepro.api.domain.strategy.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyType {
  SWING_STRATEGY("SWING_STRATEGY"),
  INTRADAY_STRATEGY("INTRADAY_STRATEGY");

  private final String name;
}
