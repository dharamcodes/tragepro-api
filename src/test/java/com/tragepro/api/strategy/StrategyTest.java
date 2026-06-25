package com.tragepro.api.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.common.constant.StrategyType;
import com.tragepro.api.strategy.constant.StrategyNameType;
import com.tragepro.api.strategy.constant.TimeFrameType;
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
    assertNotNull(StrategyNameType.valueOf("INTRADAY_VP_VWAP"));
    assertEquals(2, StrategyNameType.values().length);

    assertNotNull(TimeFrameType.valueOf("LOWER"));
    assertEquals(3, TimeFrameType.values().length);
  }
}
