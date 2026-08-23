package com.tragepro.api.strategy.core.event;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class StrategyEventSystemTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private StrategyEventPublisher publisher;

    @InjectMocks
    private StrategyEventListener listener;

    @Test
    void testPublishAndListen() {
        StrategyEvent event = new StrategyEvent("test-id", "test-message");

        publisher.publish(event);
        verify(applicationEventPublisher).publishEvent(event);

        listener.on(event);
    }
}
