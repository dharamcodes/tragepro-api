package com.tragepro.api.strategy.core;

import static org.junit.jupiter.api.Assertions.assertNull;

import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.strategy.core.pipeline.builder.*;
import com.tragepro.api.strategy.core.pipeline.evaluator.*;
import com.tragepro.api.strategy.core.pipeline.executor.*;
import org.junit.jupiter.api.Test;

class StrategyCoreComponentsTest {

    @Test
    void testBuilders() {
        StrategyRequest req = StrategyRequest.builder().build();
        assertNull(new BuildVolumeProfile().build(req));
        assertNull(new BuildVwapLevels().build(req));
        assertNull(new BuildBidAskLevels().build(req));
        assertNull(new BuildLiquidityLevels().build(req));
        assertNull(new BuildTimeframeData().build(req));
    }

    @Test
    void testEvaluators() {
        StrategyRequest req = StrategyRequest.builder().build();
        assertNull(new EvaluateVolumeVwap().evaluate(req));
        assertNull(new EvaluateLiquidityLevels().evaluate(req));
        assertNull(new EvaluateVolumeProfile().evaluate(req));
    }

    @Test
    void testExecutors() {
        StrategyRequest req = StrategyRequest.builder().build();
        assertNull(new ExecuteNotify().execute(req));
        assertNull(new ExecuteSell().execute(req));
        assertNull(new ExecuteBuy().execute(req));
    }
}
