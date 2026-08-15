package com.tragepro.api.identity.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class IdentityEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void publish(IdentityEvent event) {
    log.info("Publishing IdentityEvent: {}", event.eventId());
    applicationEventPublisher.publishEvent(event);
  }
}
