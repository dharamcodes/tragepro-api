package com.tragepro.api.strategy.core.builder;

import com.tragepro.api.strategy.core.StrategyBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component("BUILD_VWAP_LEVELS")
public class BuildVwapLevels implements StrategyBuilder {
  @Override
  public void build(long time, TimeUnit uom) {
    System.out.println("Building VWAP levels for " + time + " " + uom.toString());
  }
}
