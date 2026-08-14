package com.tragepro.api.strategy.constant;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.strategy.Strategy;
import com.tragepro.api.strategy.definition.IntradayStrategy;
import com.tragepro.api.strategy.definition.SwingStrategy;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyType {
  SWING_STRATEGY("SWING_STRATEGY", new SwingStrategy()),
  INTRADAY_STRATEGY("INTRADAY_STRATEGY", new IntradayStrategy());

  private final String name;
  private final Strategy strategy;

  public static Strategy strategy(StrategyType strategy) {
    return Arrays.stream(values())
        .filter(seg -> seg.equals(strategy))
        .map(StrategyType::getStrategy)
        .findFirst()
        .orElseThrow(() -> new AppException(ErrorType.INTERNAL_ERROR));
  }
}
