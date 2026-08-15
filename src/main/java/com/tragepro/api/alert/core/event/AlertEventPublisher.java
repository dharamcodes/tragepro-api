package com.tragepro.api.alert.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void publish(AlertEvent event) {
    log.info("Publishing AlertEvent: {}", event.eventId());
    applicationEventPublisher.publishEvent(event);
  }
}
