package com.tragepro.api.trading.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TradingEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void publish(TradingEvent event) {
    log.info("Publishing TradingEvent: {}", event.eventId());
    applicationEventPublisher.publishEvent(event);
  }
}
