package com.tragepro.api.strategy.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.strategy.model.StrategyModel;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyAdapterTest {

  @Mock private StrategyService strategyService;

  private StrategyAdapterImpl strategyAdapter;

  @BeforeEach
  void setUp() {
    strategyAdapter = new StrategyAdapterImpl(strategyService);
  }

  @Test
  void testRunStrategy() {
    StrategyModel model = StrategyModel.builder().name("IntradayV1").desc("Test Strategy").build();
    StrategyRequest request = StrategyRequest.builder().strategy(model).build();
    StrategyResponse expectedResponse = StrategyResponse.builder().strategy(model).build();
    when(strategyService.createOrUpdate(request)).thenReturn(expectedResponse);

    StrategyResponse response = strategyAdapter.runStrategy(request);

    assertNotNull(response);
    assertEquals("IntradayV1", response.getStrategy().getName());
    verify(strategyService).createOrUpdate(request);
  }
}
