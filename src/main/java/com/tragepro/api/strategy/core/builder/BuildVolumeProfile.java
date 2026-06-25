package com.tragepro.api.strategy.core.builder;

import com.tragepro.api.strategy.core.StrategyBuilder;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component("BUILD_VOLUME_PROFILE")
public class BuildVolumeProfile implements StrategyBuilder {
  @Override
  public void build(long time, TimeUnit uom) {
    System.out.println("Building volume profile for " + time + " " + uom.toString());
  }
}
