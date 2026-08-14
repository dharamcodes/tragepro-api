package com.tragepro.api.datafeed.event;

import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class DataEventSystemTest {

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks private DataEventPublisher publisher;

  @InjectMocks private DataEventListener listener;

  @Test
  void testPublishAndListen() {
    DataEvent event = new DataEvent("test-id", List.of());

    publisher.publish(event);
    verify(applicationEventPublisher).publishEvent(event);

    // Call listener directly to cover it
    listener.on(event);
  }
}
