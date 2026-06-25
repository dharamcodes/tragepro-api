package com.tragepro.api.strategy.config;

import com.tragepro.api.common.constant.TimeframeUomType;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "workflow")
public class StrategyConfig {

  private List<Strategy> strategy;

  public record Strategy(
      String name, List<Timeframe> timeframe, Map<String, List<Step>> strategySteps) {}

  public record Timeframe(int value, TimeframeUomType uom, String type) {}

  public record Step(String name, Integer time, TimeframeUomType uom, Double probability) {}
}
