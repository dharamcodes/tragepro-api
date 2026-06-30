package com.tragepro.api.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.common.constant.StrategyType;
import com.tragepro.api.strategy.constant.Timeframe;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import org.junit.jupiter.api.Test;

class StrategyTest {

  @Test
  void testIntradayStrategy() {
    Strategy strategy = new IntradayStrategy();
    assertEquals(StrategyType.INTRADAY_STRATEGY, strategy.getStrategyType());

    StrategyRequest request = StrategyRequest.builder().build();
    assertNull(strategy.builder(request));
    assertNull(strategy.evaluator(request));
    assertNull(strategy.executor(request));
  }

  @Test
  void testSwingStrategy() {
    Strategy strategy = new SwingStrategy();
    assertEquals(StrategyType.SWING_STRATEGY, strategy.getStrategyType());

    StrategyRequest request = StrategyRequest.builder().build();
    assertNull(strategy.builder(request));
    assertNull(strategy.evaluator(request));
    assertNull(strategy.executor(request));
  }

  @Test
  void testEnums() {
    assertNotNull(Timeframe.valueOf("LOWER"));
    assertEquals(3, Timeframe.values().length);
  }
}
