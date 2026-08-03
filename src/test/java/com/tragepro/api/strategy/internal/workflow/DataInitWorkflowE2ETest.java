package com.tragepro.api.strategy.internal.workflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.tragepro.api.common.ContainerConfig;
import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.constant.TimeUnit;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.datafeed.dto.LoadCandleRequest;
import com.tragepro.api.datafeed.internal.context.WatchlistContext;
import com.tragepro.api.datafeed.internal.service.DatafeedService;
import com.tragepro.api.datafeed.internal.service.SecurityService;
import com.tragepro.api.strategy.dto.CandleModel;
import com.tragepro.api.strategy.dto.StatusModel;
import com.tragepro.api.strategy.dto.StrategyModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.SymbolModel;
import com.tragepro.api.strategy.dto.WorkflowRequest;
import com.tragepro.api.strategy.dto.WorkflowResponse;
import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.constant.StrategyStep;
import com.tragepro.api.strategy.internal.context.StrategyContext;
import com.tragepro.api.strategy.internal.entity.StrategyEntity;
import com.tragepro.api.strategy.internal.repository.StrategyRepository;
import com.tragepro.api.strategy.internal.service.StrategyService;
import com.tragepro.api.strategy.internal.workflow.activity.impl.BuilderActivityImpl;
import com.tragepro.api.strategy.internal.workflow.activity.impl.DataInitActivityImpl;
import io.temporal.client.WorkflowFailedException;
import io.temporal.client.WorkflowOptions;
import io.temporal.testing.TestWorkflowEnvironment;
import io.temporal.worker.Worker;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DataInitWorkflowE2ETest extends ContainerConfig {

  @Autowired private DataInitActivityImpl dataInitActivity;
  @Autowired private BuilderActivityImpl builderActivity;
  @Autowired private WatchlistContext watchlistContext;
  @Autowired private StrategyContext strategyContext;
  @Autowired private StrategyRepository strategyRepository;
  @Autowired private StrategyService strategyService;
  @Autowired private SecurityService securityService;
  @Autowired private DatafeedService datafeedService;

  private TestWorkflowEnvironment testEnv;
  private Worker worker;
  private static final String E2E_TASK_QUEUE = "E2E_Workflow_Task_Queue";

  @BeforeEach
  void setUp() {
    strategyRepository.deleteAll();
    testEnv = TestWorkflowEnvironment.newInstance();
    worker = testEnv.newWorker(E2E_TASK_QUEUE);
    worker.registerWorkflowImplementationTypes(DataInitWorkflowImpl.class);
    worker.registerActivitiesImplementations(dataInitActivity, builderActivity);
    testEnv.start();
  }

  @AfterEach
  void tearDown() {
    if (testEnv != null) {
      testEnv.close();
    }
  }

  @Test
  @DisplayName(
      "End-to-End Test: DataInitWorkflow execution from trigger to database & context state update")
  void testDataInitWorkflow_EndToEnd_Success() {
    String watchlistName = "NIFTY_50";
    String strategyName = "INTRADAY_VP_VWAP";

    Set<SymbolDataModel> stocks =
        Set.of(
            new SymbolDataModel("AAPL", "Apple Inc."),
            new SymbolDataModel("MSFT", "Microsoft"),
            new SymbolDataModel("RELIANCE", "Reliance Industries"));
    watchlistContext.addWatchlist(watchlistName, stocks);

    DataInitWorkflow workflow =
        testEnv
            .getWorkflowClient()
            .newWorkflowStub(
                DataInitWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(E2E_TASK_QUEUE).build());

    Set<StrategyResponse> results = workflow.execute(strategyName);

    assertNotNull(results);
    assertEquals(3, results.size());

    StrategyResponse contextResponse = strategyContext.get(strategyName);
    assertNotNull(contextResponse);
    assertEquals(strategyName, contextResponse.getStrategy().getName());

    List<StrategyEntity> savedEntities = strategyRepository.findAll();
    assertFalse(savedEntities.isEmpty());
    assertEquals(3, savedEntities.size());

    assertTrue(
        savedEntities.stream()
            .allMatch(
                entity ->
                    entity.getCurrentState() != null
                        && StrategyState.INITIALIZING.equals(entity.getCurrentState().getState())));
  }

  @Test
  @DisplayName("End-to-End Test: DataInitWorkflow execution failure with invalid strategy name")
  void testDataInitWorkflow_EndToEnd_InvalidStrategyName() {
    String invalidStrategy = "INVALID_UNKNOWN_STRATEGY";

    DataInitWorkflow workflow =
        testEnv
            .getWorkflowClient()
            .newWorkflowStub(
                DataInitWorkflow.class,
                WorkflowOptions.newBuilder().setTaskQueue(E2E_TASK_QUEUE).build());

    assertThrows(WorkflowFailedException.class, () -> workflow.execute(invalidStrategy));
  }

  @Test
  @DisplayName("End-to-End Test: Strategy Service workflow execution trigger")
  void testStrategyService_RunWorkflow_EndToEnd() {
    WorkflowRequest request = WorkflowRequest.builder().strategyId("STRAT_123").build();
    WorkflowResponse response = strategyService.run(request);

    assertNotNull(response);
    assertEquals("SUCCESS", response.getStatus());
    assertNotNull(response.getMessage());
    assertNotNull(response.getResults());
  }

  @Test
  @DisplayName("End-to-End Test: BuilderActivity implementations")
  void testBuilderActivity_EndToEnd() {
    StrategyRequest request = StrategyRequest.builder().build();
    StrategyResponse response = builderActivity.run(request);
    assertNotNull(response);

    TimeframeModel timeframe = TimeframeModel.builder().value(5).uom(TimeUnit.MINUTE).build();
    assertNull(builderActivity.loadBaseCandleData(request, timeframe));
    assertNull(builderActivity.candleTimeframeConverter(CandleModel.builder().build(), timeframe));
  }

  @Test
  @DisplayName("End-to-End Test: BaseActivity evaluateState transition for existing non-null state")
  void testBaseActivity_StateTransition_EndToEnd() {
    StrategyRequest req =
        StrategyRequest.builder()
            .strategy(StrategyModel.builder().name("STRAT_ADV").watchlist("NIFTY_50").build())
            .symbolData(
                SymbolModel.builder().symbol("AAPL").name("Apple").exchange(Exchange.NSE).build())
            .currentState(
                StatusModel.builder()
                    .state(StrategyState.INITIALIZING)
                    .step(StrategyStep.INIT)
                    .build())
            .build();

    Set<StrategyRequest> stored = dataInitActivity.storeData(Set.of(req));
    assertNotNull(stored);
    assertEquals(1, stored.size());
    assertEquals(StrategyState.BUILDING, req.getCurrentState().getState());
    assertEquals(StrategyStep.BUILD, req.getCurrentState().getStep());
  }

  @Test
  @DisplayName("End-to-End Test: StrategyService parameter validation error branches")
  void testStrategyService_ValidationErrors_EndToEnd() {
    assertThrows(AppException.class, () -> strategyService.create(null));

    StrategyRequest invalidRequest =
        StrategyRequest.builder()
            .strategy(StrategyModel.builder().name("TEST").watchlist(null).build())
            .symbolData(SymbolModel.builder().symbol(null).build())
            .currentState(StatusModel.builder().state(null).build())
            .build();

    assertThrows(AppException.class, () -> strategyService.createOrUpdate(invalidRequest));
  }

  @Test
  @DisplayName("End-to-End Test: SecurityService and DatafeedService error branches")
  void testSecurityAndDatafeedServices_ErrorBranches_EndToEnd() {
    assertThrows(AppException.class, () -> securityService.fetSecurityBySymbol(""));
    assertThrows(
        AppException.class, () -> securityService.fetSecurityBySymbol("UNKNOWN_SYMBOL_XYZ"));
    assertThrows(AppException.class, () -> datafeedService.loadData(null));
    assertThrows(
        AppException.class, () -> datafeedService.loadData(new LoadCandleRequest(null, 0)));
  }
}
