package com.tragepro.api.identity.core.event;

import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class IdentityEventSystemTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private IdentityEventPublisher publisher;

    @InjectMocks
    private IdentityEventListener listener;

    @Test
    void testPublishAndListen() {
        IdentityEvent event = new IdentityEvent("test-id", "test-message");

        publisher.publish(event);
        verify(applicationEventPublisher).publishEvent(event);

        listener.on(event);
    }
}
