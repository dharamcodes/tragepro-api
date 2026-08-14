package com.tragepro.api.strategy.core.workflow.activity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.datafeed.constant.TimeUnit;
import com.tragepro.api.datafeed.constant.Timeframe;
import com.tragepro.api.datafeed.context.WatchlistContext;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import com.tragepro.api.strategy.ConfigLoaderService;
import com.tragepro.api.strategy.StrategyService;
import com.tragepro.api.strategy.constant.IndicatorType;
import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.constant.StrategyStep;
import com.tragepro.api.strategy.context.StrategyContext;
import com.tragepro.api.strategy.internal.mapper.StrategyMapper;
import com.tragepro.api.strategy.model.StatusModel;
import com.tragepro.api.strategy.model.StrategyModel;
import com.tragepro.api.strategy.model.SymbolModel;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.props.IndicatorConfig;
import com.tragepro.api.strategy.props.StrategyConfig;
import com.tragepro.api.strategy.props.TimeframeConfig;
import com.tragepro.api.strategy.workflow.activity.impl.DataInitActivityImpl;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataInitActivityImplTest {

  @Mock private ConfigLoaderService configLoaderService;
  @Mock private WatchlistContext watchlistContext;
  @Mock private StrategyContext strategyContext;
  @Mock private StrategyService strategyService;
  @Mock private MapperFactory mapperFactory;
  @Mock private StrategyMapper strategyMapper;

  @InjectMocks private DataInitActivityImpl dataInitActivity;

  private StrategyRequest strategyRequest;
  private StrategyResponse strategyResponse;
  private StrategyEntity strategyEntity;

  @BeforeEach
  void setUp() {
    strategyRequest =
        StrategyRequest.builder()
            .strategy(StrategyModel.builder().watchlist("WL").build())
            .symbolData(SymbolModel.builder().symbol("SYM").build())
            .currentState(
                StatusModel.builder()
                    .state(StrategyState.INITIALIZING)
                    .step(StrategyStep.INIT)
                    .build())
            .build();
    strategyResponse =
        StrategyResponse.builder()
            .strategy(StrategyModel.builder().name("STRATEGY_NAME").build())
            .symbolData(SymbolModel.builder().symbol("SYM").build())
            .build();
    strategyEntity = new StrategyEntity();
  }

  @Test
  void testRun() {
    when(mapperFactory.getMapper(StrategyMapper.class)).thenReturn(strategyMapper);
    when(strategyMapper.requestToEntity(strategyRequest)).thenReturn(strategyEntity);
    when(strategyMapper.entityToResponse(strategyEntity)).thenReturn(strategyResponse);

    Set<StrategyResponse> result = dataInitActivity.run(Set.of(strategyRequest));
    assertNotNull(result);
    assertEquals(1, result.size());
    verify(strategyContext, times(1)).put(eq("STRATEGY_NAME"), eq(strategyResponse));
  }

  @Test
  void testStoreData() {
    when(strategyService.createOrUpdate(strategyRequest)).thenReturn(strategyResponse);
    when(mapperFactory.getMapper(StrategyMapper.class)).thenReturn(strategyMapper);
    when(strategyMapper.responseToRequest(strategyResponse)).thenReturn(strategyRequest);

    Set<StrategyRequest> result = dataInitActivity.storeData(Set.of(strategyRequest));
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testLoadConfig() {
    StrategyConfig config = new StrategyConfig();
    config.setName("StrategyName");
    config.setDesc("Desc");
    config.setWatchList("WL");

    IndicatorConfig indicator = new IndicatorConfig();
    indicator.setName(IndicatorType.VWAP_LEVELS);
    indicator.setTimeFrame(Timeframe.LOWER);
    config.setIndicators(Set.of(indicator));

    TimeframeConfig timeframe = new TimeframeConfig();
    timeframe.setValue(1);
    timeframe.setUom(TimeUnit.MINUTE);
    timeframe.setType(Timeframe.LOWER);
    config.setTimeframes(List.of(timeframe));

    when(configLoaderService.getStrategyByName("StrategyName")).thenReturn(config);

    StrategyRequest result = dataInitActivity.loadConfig("StrategyName");
    assertNotNull(result);
    assertEquals("StrategyName", result.getStrategy().getName());
    assertEquals("Desc", result.getStrategy().getDesc());
    assertEquals("WL", result.getStrategy().getWatchlist());
  }

  @Test
  void testLoadSymbol() {
    SymbolDataModel symbolData = new SymbolDataModel("AAPL", "Apple");
    when(watchlistContext.getWatchlist("WL")).thenReturn(Set.of(symbolData));

    Set<StrategyRequest> result = dataInitActivity.loadSymbol(strategyRequest);
    assertNotNull(result);
    assertEquals(1, result.size());
  }

  @Test
  void testGlobalAndLocalActivities() {
    assertNotNull(dataInitActivity.globalActivities());
    assertNotNull(dataInitActivity.localActivities());
  }
}
