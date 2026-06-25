package com.tragepro.api.strategy.core.builder;

import com.tragepro.api.strategy.core.StrategyBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component("BUILD_TIMEFRAME_DATA")
public class BuildTimeframeData implements StrategyBuilder {
  @Override
  public void build(long time, TimeUnit uom) {
    System.out.println("Building timeframe data for " + time + " " + uom.toString());
  }
}
