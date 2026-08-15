package com.tragepro.api.strategy.core.workflow.activity;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.workflow.activity.impl.BuilderActivityImpl;
import org.junit.jupiter.api.Test;

class BuilderActivityImplTest {

  private final BuilderActivityImpl builderActivity = new BuilderActivityImpl();

  @Test
  void testRun() {
    StrategyResponse response = builderActivity.run(StrategyRequest.builder().build());
    assertNotNull(response);
  }

  @Test
  void testLoadBaseCandleData() {
    assertNull(builderActivity.loadBaseCandleData(null, null));
  }

  @Test
  void testCandleTimeframeConverter() {
    assertNull(builderActivity.candleTimeframeConverter(null, null));
  }

  @Test
  void testGlobalAndLocalActivities() {
    assertNotNull(builderActivity.globalActivities());
    assertNotNull(dataInitActivityGlobalActivities());
  }

  private Object dataInitActivityGlobalActivities() {
    return builderActivity.localActivities();
  }
}
