package com.tragepro.api.strategy.internal.event;

import com.tragepro.api.strategy.StrategyEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StrategyEventListener {

  @Async
  @ApplicationModuleListener
  public void on(StrategyEvent event) {}
}
