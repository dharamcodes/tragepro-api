package com.tragepro.api.journal.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JournalEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void publish(JournalEvent event) {
    log.info("Publishing JournalEvent: {}", event.eventId());
    applicationEventPublisher.publishEvent(event);
  }
}
