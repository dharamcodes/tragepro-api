package com.tragepro.api.strategy.context;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class StrategyContextConfigTest {

  @Test
  void testGetAndPut() {
    Map<SymbolData, StrategyResponse> contextMap = new HashMap<>();
    StrategyContextConfig config = new StrategyContextConfig(contextMap);

    SymbolData key = new SymbolData("AAPL", "Apple");
    StrategyResponse response = StrategyResponse.builder().build();

    assertNull(config.get(key));
    config.put(key, response);
    assertEquals(response, config.get(key));
  }
}
