package com.tragepro.api.strategy.internal.initializer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.common.MockDataFactory;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.internal.context.StrategyContext;
import com.tragepro.api.strategy.internal.service.StrategyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StrategyInitializerTest extends ContainerConfig {

  @Autowired private StrategyInitializer strategyInitializer;
  @Autowired private StrategyService strategyService;
  @Autowired private StrategyContext strategyContext;

  @Test
  void testStrategyInitializerWithMockData() throws Exception {
    StrategyRequest strategyReq =
        MockDataFactory.createStrategyRequest("INIT_STRATEGY", "INIT_STOCK", "WL_INITIALIZER");
    strategyService.createOrUpdate(strategyReq);

    strategyInitializer.run();

    var strategyResponse = strategyContext.get("INIT_STRATEGY");
    assertNotNull(strategyResponse);
    assertEquals("INIT_STRATEGY", strategyResponse.getStrategy().getName());
  }
}
