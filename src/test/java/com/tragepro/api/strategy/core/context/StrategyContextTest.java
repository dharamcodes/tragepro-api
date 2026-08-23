package com.tragepro.api.strategy.core.context;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.domain.strategy.response.StrategyResponse;
import org.junit.jupiter.api.Test;

class StrategyContextTest {

    @Test
    void testGetAndPut() {
        StrategyContext config = new StrategyContext();

        String key = "test-strategy";
        StrategyResponse response = StrategyResponse.builder().build();

        assertNull(config.get(key));
        config.put(key, response);
        assertEquals(response, config.get(key));
    }
}
