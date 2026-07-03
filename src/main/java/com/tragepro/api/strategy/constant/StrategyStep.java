package com.tragepro.api.strategy.constant;

import java.util.Arrays;
import java.util.Optional;
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

  public static Optional<StrategyStep> nextStep(StrategyStep current) {
    int nextPriority = current.priority + 1;
    return Arrays.stream(values()).filter(s -> s.priority == nextPriority).findFirst();
  }
}
