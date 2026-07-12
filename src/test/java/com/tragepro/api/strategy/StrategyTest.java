package com.tragepro.api.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tragepro.api.common.constant.IndicatorType;
import com.tragepro.api.common.constant.StrategyType;
import com.tragepro.api.common.constant.Timeframe;
import com.tragepro.api.strategy.constant.StrategyBuilderStep;
import com.tragepro.api.strategy.constant.StrategyEvaluatorStep;
import com.tragepro.api.strategy.constant.StrategyExecutorStep;
import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.constant.StrategyStep;
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

    // StrategyBuilderStep
    assertNotNull(StrategyBuilderStep.valueOf("BUILD_VOLUME_PROFILE"));
    assertEquals(
        StrategyBuilderStep.BUILD_VOLUME_PROFILE, StrategyBuilderStep.of("BUILD_VOLUME_PROFILE"));
    assertNull(StrategyBuilderStep.of(null));
    assertNull(StrategyBuilderStep.of("UNKNOWN"));

    // StrategyStep
    assertNotNull(StrategyStep.valueOf("INIT"));
    assertTrue(StrategyStep.nextStep(StrategyStep.INIT).isPresent());
    assertEquals(StrategyStep.BUILD, StrategyStep.nextStep(StrategyStep.INIT).get());

    // StrategyEvaluatorStep
    assertNotNull(StrategyEvaluatorStep.valueOf("EVALUATE_VOLUME_PROFILE"));
    assertEquals(
        StrategyEvaluatorStep.EVALUATE_VOLUME_PROFILE,
        StrategyEvaluatorStep.of("EVALUATE_VOLUME_PROFILE"));
    assertNull(StrategyEvaluatorStep.of(null));
    assertNull(StrategyEvaluatorStep.of("UNKNOWN"));

    // StrategyExecutorStep
    assertNotNull(StrategyExecutorStep.valueOf("EXECUTE_BUY"));
    assertEquals(StrategyExecutorStep.EXECUTE_BUY, StrategyExecutorStep.of("EXECUTE_BUY"));
    assertNull(StrategyExecutorStep.of(null));
    assertNull(StrategyExecutorStep.of("UNKNOWN"));

    // StrategyState
    assertNotNull(StrategyState.valueOf("INITIALIZING"));
    assertTrue(StrategyState.nextState(StrategyState.INITIALIZING).isPresent());
    assertEquals(StrategyState.BUILDING, StrategyState.nextState(StrategyState.INITIALIZING).get());
    assertFalse(StrategyState.nextState(StrategyState.OBSERVING_EXECUTE).isPresent());

    // IndicatorType
    assertNotNull(IndicatorType.valueOf("VOLUME_PROFILE"));
    assertEquals(IndicatorType.VOLUME_PROFILE, IndicatorType.of("VOLUME_PROFILE"));
    assertThrows(IllegalArgumentException.class, () -> IndicatorType.of("UNKNOWN"));
  }
}
