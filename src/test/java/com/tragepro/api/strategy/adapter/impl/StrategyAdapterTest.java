package com.tragepro.api.strategy.adapter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.domain.strategy.StrategyModel;
import com.tragepro.api.domain.strategy.request.StrategyRequest;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.props.StrategyConfig;
import com.tragepro.api.strategy.service.ConfigLoaderService;
import com.tragepro.api.strategy.service.StrategyService;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyAdapterTest {

    @Mock
    private StrategyService strategyService;

    @Mock
    private ConfigLoaderService configLoaderService;

    private StrategyAdapterImpl strategyAdapter;
    private ConfigLoaderAdapterImpl configLoaderAdapter;

    @BeforeEach
    void setUp() {
        strategyAdapter = new StrategyAdapterImpl(strategyService);
        configLoaderAdapter = new ConfigLoaderAdapterImpl(configLoaderService);
    }

    @Test
    void testStrategyAdapterMethods() {
        StrategyModel model =
                StrategyModel.builder().name("IntradayV1").desc("Test Strategy").build();
        StrategyRequest request = StrategyRequest.builder().strategy(model).build();
        StrategyResponse expectedResponse =
                StrategyResponse.builder().strategy(model).build();

        when(strategyService.create(request)).thenReturn(expectedResponse);
        assertEquals(expectedResponse, strategyAdapter.create(request));

        when(strategyService.createOrUpdate(request)).thenReturn(expectedResponse);
        StrategyResponse response = strategyAdapter.createOrUpdate(request);
        assertNotNull(response);
        assertEquals("IntradayV1", response.getStrategy().getName());
        verify(strategyService).createOrUpdate(request);

        when(strategyService.getAll()).thenReturn(Set.of(expectedResponse));
        assertEquals(1, strategyAdapter.getAll().size());
    }

    @Test
    void testConfigLoaderAdapterMethods() {
        StrategyConfig config = new StrategyConfig();
        config.setName("IntradayV1");

        when(configLoaderService.getStrategyByName("IntradayV1")).thenReturn(config);
        StrategyConfig result = configLoaderAdapter.getStrategyByName("IntradayV1");
        assertNotNull(result);
        assertEquals("IntradayV1", result.getName());
        verify(configLoaderService).getStrategyByName("IntradayV1");
    }
}
