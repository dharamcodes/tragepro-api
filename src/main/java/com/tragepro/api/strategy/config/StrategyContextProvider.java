package com.tragepro.api.strategy.config;

import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyContextProvider {

  @Bean
  public Map<SymbolData, StrategyResponse> strategyContext() {
    return new HashMap<>();
  }
}
