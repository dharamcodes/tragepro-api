package com.tragepro.api.trading.event;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class TradingEventSystemTest {

  @Mock private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks private TradingEventPublisher publisher;

  @InjectMocks private TradingEventListener listener;

  @Test
  void testPublishAndListen() {
    TradingEvent event = new TradingEvent("test-id", "test-message");

    publisher.publish(event);
    verify(applicationEventPublisher).publishEvent(event);

    listener.on(event);
  }
}
