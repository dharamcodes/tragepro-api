package com.tragepro.api.journal.internal.event;

import com.tragepro.api.journal.JournalEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JournalEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publish(JournalEvent event) {
    eventPublisher.publishEvent(event);
  }
}
