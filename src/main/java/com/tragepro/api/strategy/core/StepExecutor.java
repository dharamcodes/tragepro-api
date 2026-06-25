package com.tragepro.api.strategy.core;

import com.tragepro.api.common.constant.TimeframeUomType;
import com.tragepro.api.strategy.config.StrategyConfig;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StepExecutor {

  private final StrategyConfig strategyConfig;
  private final ApplicationContext applicationContext;

  public void executeStrategies() {
    strategyConfig
        .getStrategy()
        .forEach(
            strategy -> {
              strategy
                  .strategySteps()
                  .get("strategyBuilder")
                  .forEach(
                      step -> {
                        StrategyBuilder builder =
                            (StrategyBuilder) applicationContext.getBean(step.name());
                        builder.build(step.time(), toTimeUnit(step.uom()));
                      });

              strategy
                  .strategySteps()
                  .get("strategyEvaluator")
                  .forEach(
                      step -> {
                        StrategyEvaluator evaluator =
                            (StrategyEvaluator) applicationContext.getBean(step.name());
                        evaluator.evaluate(step.probability());
                      });

              strategy
                  .strategySteps()
                  .get("strategyExecutor")
                  .forEach(
                      step -> {
                        StrategyExecutor executor =
                            (StrategyExecutor) applicationContext.getBean(step.name());
                        executor.execute();
                      });
            });
  }

  private TimeUnit toTimeUnit(TimeframeUomType uom) {
    return switch (uom) {
      case SEC -> TimeUnit.SECONDS;
      case MIN -> TimeUnit.MINUTES;
      case HR -> TimeUnit.HOURS;
      case DAY -> TimeUnit.DAYS;
      default -> throw new IllegalArgumentException("Unsupported time unit: " + uom);
    };
  }
}
