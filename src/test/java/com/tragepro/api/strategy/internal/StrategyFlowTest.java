package com.tragepro.api.strategy.internal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.strategy.StrategyType;
import com.tragepro.api.strategy.dto.CandleModel;
import com.tragepro.api.strategy.dto.IndicatorModel;
import com.tragepro.api.strategy.dto.StatusModel;
import com.tragepro.api.strategy.dto.StrategyModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.SymbolModel;
import com.tragepro.api.strategy.internal.builder.BuildBidAskLevels;
import com.tragepro.api.strategy.internal.builder.BuildLiquidityLevels;
import com.tragepro.api.strategy.internal.builder.BuildTimeframeData;
import com.tragepro.api.strategy.internal.builder.BuildVolumeProfile;
import com.tragepro.api.strategy.internal.builder.BuildVwapLevels;
import com.tragepro.api.strategy.internal.constant.StrategyBuilderStep;
import com.tragepro.api.strategy.internal.constant.StrategyEvaluatorStep;
import com.tragepro.api.strategy.internal.constant.StrategyExecutorStep;
import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.constant.StrategyStep;
import com.tragepro.api.strategy.internal.context.StrategyContext;
import com.tragepro.api.strategy.internal.entity.StrategyEntity;
import com.tragepro.api.strategy.internal.evaluator.EvaluateLiquidityLevels;
import com.tragepro.api.strategy.internal.evaluator.EvaluateVolumeProfile;
import com.tragepro.api.strategy.internal.evaluator.EvaluateVolumeVwap;
import com.tragepro.api.strategy.internal.executor.ExecuteBuy;
import com.tragepro.api.strategy.internal.executor.ExecuteNotify;
import com.tragepro.api.strategy.internal.executor.ExecuteSell;
import com.tragepro.api.strategy.internal.initializer.StrategyInitializer;
import com.tragepro.api.strategy.internal.mapper.StrategyMapper;
import com.tragepro.api.strategy.internal.service.ConfigLoaderService;
import com.tragepro.api.strategy.internal.service.StrategyService;
import com.tragepro.api.strategy.internal.strategy.IntradayStrategy;
import com.tragepro.api.strategy.internal.strategy.Strategy;
import com.tragepro.api.strategy.internal.strategy.SwingStrategy;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class StrategyFlowTest extends ContainerConfig {

  @Autowired private StrategyService strategyService;
  @Autowired private ConfigLoaderService configLoaderService;
  @Autowired private StrategyInitializer strategyInitializer;
  @Autowired private MapperFactory<StrategyMapper> mapperFactory;

  @Test
  void testStrategyTypeEnumMethods() {
    Strategy swing = StrategyType.strategy(StrategyType.SWING_STRATEGY);
    assertNotNull(swing);
    assertEquals(StrategyType.SWING_STRATEGY, swing.getStrategyType());

    Strategy intraday = StrategyType.strategy(StrategyType.INTRADAY_STRATEGY);
    assertNotNull(intraday);
    assertEquals(StrategyType.INTRADAY_STRATEGY, intraday.getStrategyType());

    assertEquals("SWING_STRATEGY", StrategyType.SWING_STRATEGY.getName());
    assertEquals("INTRADAY_STRATEGY", StrategyType.INTRADAY_STRATEGY.getName());
  }

  @Test
  void testSwingAndIntradayStrategyMethods() {
    StrategyRequest req = new StrategyRequest();
    SwingStrategy swing = new SwingStrategy();
    assertEquals(StrategyType.SWING_STRATEGY, swing.getStrategyType());
    assertDoesNotThrow(() -> swing.builder(req));
    assertDoesNotThrow(() -> swing.evaluator(req));
    assertDoesNotThrow(() -> swing.executor(req));

    IntradayStrategy intraday = new IntradayStrategy();
    assertEquals(StrategyType.INTRADAY_STRATEGY, intraday.getStrategyType());
    assertDoesNotThrow(() -> intraday.builder(req));
    assertDoesNotThrow(() -> intraday.evaluator(req));
    assertDoesNotThrow(() -> intraday.executor(req));
  }

  @Test
  void testStrategyContextAndInitializer() throws Exception {
    StrategyContext ctx = new StrategyContext();
    assertNull(ctx.get("test"));
    ctx.put("test", StrategyResponse.builder().build());
    assertNotNull(ctx.get("test"));

    assertDoesNotThrow(() -> strategyInitializer.run());
  }

  @Test
  void testConfigLoaderService() {
    assertThrows(
        AppException.class, () -> configLoaderService.getStrategyByName("nonExistentStrategy"));
  }

  @Test
  void testStrategyMapperAllMethods() {
    StrategyMapper mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    assertEquals(MapperType.STRATEGY_BUILDER_MAPPER, mapper.getType());

    SymbolModel symModel =
        SymbolModel.builder().symbol("AAPL").name("Apple").exchange(Exchange.NSE).build();
    SymbolDataModel symData = mapper.toSymbolData(symModel);
    assertEquals("AAPL", symData.symbol());

    StrategyRequest req =
        StrategyRequest.builder()
            .strategy(StrategyModel.builder().name("Test").desc("Desc").watchlist("WL1").build())
            .symbolData(symModel)
            .candleData(CandleModel.builder().build())
            .currentState(
                StatusModel.builder()
                    .state(StrategyState.BUILDING)
                    .step(StrategyStep.BUILD)
                    .build())
            .indicators(Set.of(IndicatorModel.builder().build()))
            .timeframes(Set.of(TimeframeModel.builder().build()))
            .build();

    StrategyEntity entity = mapper.requestToEntity(req);
    assertNotNull(entity);

    StrategyResponse resp = mapper.entityToResponse(entity);
    assertNotNull(resp);

    StrategyRequest mappedBack = mapper.responseToRequest(resp);
    assertNotNull(mappedBack);

    StrategyEntity target = new StrategyEntity();
    mapper.merge(req, target);
    assertEquals(entity.getStrategy(), target.getStrategy());
  }

  @Test
  void testStrategyStepsAndComponents() {
    StrategyRequest req = new StrategyRequest();

    new BuildBidAskLevels().build(req);
    new BuildLiquidityLevels().build(req);
    new BuildTimeframeData().build(req);
    new BuildVolumeProfile().build(req);
    new BuildVwapLevels().build(req);

    new EvaluateLiquidityLevels().evaluate(req);
    new EvaluateVolumeProfile().evaluate(req);
    new EvaluateVolumeVwap().evaluate(req);

    new ExecuteBuy().execute(req);
    new ExecuteNotify().execute(req);
    new ExecuteSell().execute(req);

    for (StrategyBuilderStep step : StrategyBuilderStep.values()) {
      assertNotNull(step.getName());
    }

    for (StrategyEvaluatorStep step : StrategyEvaluatorStep.values()) {
      assertNotNull(step.getName());
    }

    for (StrategyExecutorStep step : StrategyExecutorStep.values()) {
      assertNotNull(step.getName());
    }

    for (StrategyStep step : StrategyStep.values()) {
      assertNotNull(step.getName());
      assertNotNull(StrategyStep.nextStep(step));
    }

    for (StrategyState state : StrategyState.values()) {
      assertNotNull(state.getName());
      assertNotNull(StrategyState.nextState(state));
    }
  }
}
