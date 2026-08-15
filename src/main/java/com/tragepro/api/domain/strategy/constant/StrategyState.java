package com.tragepro.api.domain.strategy.constant;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StrategyState {
  INITIALIZING("INITIALIZING", 0),
  BUILDING("BUILDING", 1),
  EVALUATING("EVALUATING", 2),
  OBSERVING_EVALUATE("OBSERVING_EVALUATE", 3),
  EXECUTING("EXECUTING", 4),
  OBSERVING_EXECUTE("OBSERVING_EXECUTE", 5);

  private final String name;
  private final int priority;

  public static Optional<StrategyState> nextState(StrategyState current) {
    return Arrays.stream(values()).filter(s -> s.priority == current.priority + 1).findFirst();
  }
}
