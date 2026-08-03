package com.tragepro.api.strategy.internal.initializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tragepro.api.common.util.JsonReader;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.internal.context.StrategyContext;
import com.tragepro.api.strategy.internal.service.StrategyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyInitializer implements CommandLineRunner {

  private final StrategyService strategyService;
  private final StrategyContext strategyContext;

  @Override
  public void run(String... args) throws Exception {
    initializeStrategies();
    populateContextFromService();
  }

  private void initializeStrategies() {
    List<StrategyRequest> strategies =
        JsonReader.readJson(
            "__files/strategies.json", new TypeReference<List<StrategyRequest>>() {});
    if (strategies != null && !strategies.isEmpty()) {
      strategies.forEach(strategyService::createOrUpdate);
    }
  }

  private void populateContextFromService() {
    try {
      var allStrategies = strategyService.getAll();
      if (allStrategies != null) {
        allStrategies.forEach(
            resp -> {
              if (resp != null
                  && resp.getStrategy() != null
                  && resp.getStrategy().getName() != null) {
                strategyContext.put(resp.getStrategy().getName(), resp);
              }
            });
      }
    } catch (Exception ignored) {
    }
  }
}
