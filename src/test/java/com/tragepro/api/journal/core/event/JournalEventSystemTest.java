package com.tragepro.api.journal.core.event;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class JournalEventSystemTest {

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks private JournalEventPublisher publisher;

  @InjectMocks private JournalEventListener listener;

  @Test
  void testPublishAndListen() {
    JournalEvent event = new JournalEvent("test-id", "test-message");

    publisher.publish(event);
    verify(applicationEventPublisher).publishEvent(event);

    listener.on(event);
  }
}
