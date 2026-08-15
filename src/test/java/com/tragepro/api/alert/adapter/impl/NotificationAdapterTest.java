package com.tragepro.api.alert.adapter.impl;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.alert.core.channel.NotificationChannel;
import com.tragepro.api.alert.core.channel.NotificationChannelFactory;
import com.tragepro.api.domain.alert.NotificationChannelType;
import com.tragepro.api.domain.alert.NotificationPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationAdapterTest {

  @Mock private NotificationChannelFactory notificationChannelFactory;

  @Mock private NotificationChannel notificationChannel;

  private NotificationAdapterImpl notificationAdapter;

  @BeforeEach
  void setUp() {
    notificationAdapter = new NotificationAdapterImpl(notificationChannelFactory);
  }

  @Test
  void testSendNotification() {
    NotificationPayload payload =
        NotificationPayload.builder()
            .channelType(NotificationChannelType.EMAIL)
            .message("Test Alert")
            .build();
    when(notificationChannelFactory.getChannel(NotificationChannelType.EMAIL))
        .thenReturn(notificationChannel);

    notificationAdapter.sendNotification(payload);

    verify(notificationChannelFactory).getChannel(NotificationChannelType.EMAIL);
    verify(notificationChannel).send(payload);
  }
}
