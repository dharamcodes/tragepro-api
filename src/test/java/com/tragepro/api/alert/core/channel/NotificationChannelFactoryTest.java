package com.tragepro.api.alert.core.channel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.domain.alert.NotificationChannelType;
import com.tragepro.api.domain.alert.NotificationPayload;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationChannelFactoryTest {

    @Mock
    private NotificationChannel emailChannel;

    private NotificationChannelFactory factory;

    @BeforeEach
    void setUp() {
        when(emailChannel.getChannelType()).thenReturn(NotificationChannelType.EMAIL);
        factory = new NotificationChannelFactory(List.of(emailChannel));
    }

    @Test
    void testGetChannel_Success() {
        NotificationChannel channel = factory.getChannel(NotificationChannelType.EMAIL);
        assertEquals(emailChannel, channel);
    }

    @Test
    void testGetChannel_NotFound_ThrowsAppException() {
        assertThrows(AppException.class, () -> factory.getChannel(NotificationChannelType.TELEGRAM));
    }

    @Test
    void testEmailChannelSend() {
        EmailNotificationChannel channel = new EmailNotificationChannel();
        assertEquals(NotificationChannelType.EMAIL, channel.getChannelType());

        NotificationPayload payload = NotificationPayload.builder()
                .recipient("user@test.com")
                .subject("Sub")
                .message("Msg")
                .channelType(NotificationChannelType.EMAIL)
                .build();

        channel.send(payload);
    }

    @Test
    void testTelegramChannelSend() {
        TelegramNotificationChannel channel = new TelegramNotificationChannel();
        assertEquals(NotificationChannelType.TELEGRAM, channel.getChannelType());

        NotificationPayload payload = NotificationPayload.builder()
                .recipient("chat123")
                .subject("Sub")
                .message("Msg")
                .channelType(NotificationChannelType.TELEGRAM)
                .build();

        channel.send(payload);
    }

    @Test
    void testWebhookChannelSend() {
        WebhookNotificationChannel channel = new WebhookNotificationChannel();
        assertEquals(NotificationChannelType.WEBHOOK, channel.getChannelType());

        NotificationPayload payload = NotificationPayload.builder()
                .recipient("https://example.com/webhook")
                .subject("Sub")
                .message("Msg")
                .channelType(NotificationChannelType.WEBHOOK)
                .build();

        channel.send(payload);
    }
}
