package com.tragepro.api.strategy.core.builder;

import com.tragepro.api.strategy.core.StrategyBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component("BUILD_LIQUIDITY_LEVELS")
public class BuildLiquidityLevels implements StrategyBuilder {
  @Override
  public void build(long time, TimeUnit uom) {
    System.out.println("Building liquidity levels for " + time + " " + uom.toString());
  }
}
