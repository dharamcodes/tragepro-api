package com.tragepro.api.strategy.core.builder;

import com.tragepro.api.strategy.core.StrategyBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component("BUILD_BID_ASK_LEVELS")
public class BuildBidAskLevels implements StrategyBuilder {
  @Override
  public void build(long time, TimeUnit uom) {
    System.out.println("Building bid ask levels for " + time + " " + uom.toString());
  }
}
