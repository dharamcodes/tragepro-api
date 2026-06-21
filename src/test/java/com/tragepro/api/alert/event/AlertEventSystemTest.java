package com.tragepro.api.alert.event;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class AlertEventSystemTest {

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks private AlertEventPublisher publisher;

  @InjectMocks private AlertEventListener listener;

  @Test
  void testPublishAndListen() {
    AlertEvent event = new AlertEvent("test-id", "test-message");

    publisher.publish(event);
    verify(applicationEventPublisher).publishEvent(event);

    listener.on(event);
  }
}
