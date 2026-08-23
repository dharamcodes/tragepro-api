package com.tragepro.api.strategy.core.definition;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.domain.strategy.StrategyModel;
import com.tragepro.api.domain.strategy.response.StrategyResponse;
import com.tragepro.api.strategy.core.StrategyInitializer;
import com.tragepro.api.strategy.core.context.StrategyContext;
import com.tragepro.api.strategy.service.StrategyService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StrategyInitializerTest {

    @Mock
    private StrategyService strategyService;

    @Mock
    private StrategyContext strategyContext;

    @InjectMocks
    private StrategyInitializer strategyInitializer;

    @Test
    void testRun() {
        StrategyResponse response = StrategyResponse.builder()
                .strategy(StrategyModel.builder().name("TestStrategy").build())
                .build();
        when(strategyService.getAll()).thenReturn(Set.of(response));

        assertDoesNotThrow(() -> strategyInitializer.run());

        verify(strategyContext).put("TestStrategy", response);
    }
}
