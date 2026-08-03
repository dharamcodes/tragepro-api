package com.tragepro.api.strategy.internal.event;

import com.tragepro.api.strategy.StrategyEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StrategyEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publish(StrategyEvent event) {
    eventPublisher.publishEvent(event);
  }
}
