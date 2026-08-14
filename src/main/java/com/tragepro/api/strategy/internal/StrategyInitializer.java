package com.tragepro.api.strategy.internal;

import com.tragepro.api.strategy.context.StrategyContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyInitializer implements CommandLineRunner {

  private final StrategyService strategyService;
  private final StrategyContext strategyContext;

  @Override
  public void run(String @NonNull ... args) throws Exception {
    strategyService
        .getAll()
        .forEach(
            strategy -> {
              log.info("loading strategy - name :: {}", strategy.getStrategy().getName());
              strategyContext.put(strategy.getStrategy().getName(), strategy);
            });
  }
}
