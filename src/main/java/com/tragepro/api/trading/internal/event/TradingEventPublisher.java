package com.tragepro.api.trading.internal.event;

import com.tragepro.api.trading.TradingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradingEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publish(TradingEvent event) {
    eventPublisher.publishEvent(event);
  }
}
