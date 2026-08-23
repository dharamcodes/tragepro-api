package com.tragepro.api.alert.core.event;

import static org.mockito.Mockito.*;

import com.tragepro.api.alert.core.channel.NotificationChannel;
import com.tragepro.api.alert.core.channel.NotificationChannelFactory;
import com.tragepro.api.domain.alert.NotificationChannelType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlertEventListenerTest {

    @Mock
    private NotificationChannelFactory notificationChannelFactory;

    @Mock
    private NotificationChannel notificationChannel;

    private AlertEventListener alertEventListener;

    @BeforeEach
    void setUp() {
        alertEventListener = new AlertEventListener(notificationChannelFactory);
    }

    @Test
    void testOn_Success() {
        AlertEvent event = new AlertEvent("event-1", "System alert");
        when(notificationChannelFactory.getChannel(NotificationChannelType.EMAIL))
                .thenReturn(notificationChannel);

        alertEventListener.on(event);

        verify(notificationChannelFactory).getChannel(NotificationChannelType.EMAIL);
        verify(notificationChannel).send(any());
    }

    @Test
    void testOn_Exception() {
        AlertEvent event = new AlertEvent("event-2", "Error alert");
        when(notificationChannelFactory.getChannel(any())).thenThrow(new RuntimeException("Channel lookup failed"));

        // Ensure exception is handled and does not propagate
        alertEventListener.on(event);

        verify(notificationChannelFactory).getChannel(NotificationChannelType.EMAIL);
    }
}
