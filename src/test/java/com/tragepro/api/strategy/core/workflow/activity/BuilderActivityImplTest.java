package com.tragepro.api.strategy.core.workflow.activity;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.strategy.core.workflow.activity.impl.BuilderActivityImpl;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
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
}
