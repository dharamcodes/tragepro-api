package com.tragepro.api.strategy.core;

import static org.mockito.Mockito.verify;

import com.tragepro.api.strategy.core.builder.*;
import com.tragepro.api.strategy.core.evaluator.*;
import com.tragepro.api.strategy.core.executor.ExecuteBuy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
class StepExecutorTest {

  @Autowired private StepExecutor stepExecutor;

  @MockitoSpyBean(name = "BUILD_TIMEFRAME_DATA")
  private BuildTimeframeData buildTimeframeData;

  @MockitoSpyBean(name = "BUILD_VOLUME_PROFILE")
  private BuildVolumeProfile buildVolumeProfile;

  @MockitoSpyBean(name = "BUILD_BID_ASK_LEVELS")
  private BuildBidAskLevels buildBidAskLevels;

  @MockitoSpyBean(name = "BUILD_VWAP_LEVELS")
  private BuildVwapLevels buildVwapLevels;

  @MockitoSpyBean(name = "BUILD_LIQUIDITY_LEVELS")
  private BuildLiquidityLevels buildLiquidityLevels;

  @MockitoSpyBean(name = "EVALUATE_VOLUME_PROFILE")
  private EvaluateVolumeProfile evaluateVolumeProfile;

  @MockitoSpyBean(name = "EVALUATE_VOLUME_VWAP")
  private EvaluateVolumeVwap evaluateVolumeVwap;

  @MockitoSpyBean(name = "EVALUATE_LIQUIDITY_LEVELS")
  private EvaluateLiquidityLevels evaluateLiquidityLevels;

  @MockitoSpyBean(name = "EXECUTE_BUY")
  private ExecuteBuy executeBuy;

  @Test
  void testExecuteStrategies() {
    stepExecutor.executeStrategies();

    verify(buildTimeframeData).build(50, java.util.concurrent.TimeUnit.DAYS);
    verify(buildVolumeProfile).build(50, java.util.concurrent.TimeUnit.DAYS);
    verify(buildBidAskLevels).build(50, java.util.concurrent.TimeUnit.DAYS);
    verify(buildVwapLevels).build(50, java.util.concurrent.TimeUnit.DAYS);
    verify(buildLiquidityLevels).build(50, java.util.concurrent.TimeUnit.DAYS);

    verify(evaluateVolumeProfile).evaluate(0.2);
    verify(evaluateVolumeVwap).evaluate(0.1);
    verify(evaluateLiquidityLevels).evaluate(0.4);

    verify(executeBuy).execute();
  }
}
