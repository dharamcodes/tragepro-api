package com.tragepro.api.alert.internal;

import com.tragepro.api.alert.NotificationAdapter;
import com.tragepro.api.alert.channel.NotificationChannel;
import com.tragepro.api.alert.channel.NotificationChannelFactory;
import com.tragepro.api.alert.model.NotificationPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class NotificationAdapterImpl implements NotificationAdapter {

  private final NotificationChannelFactory notificationChannelFactory;

  @Override
  public void sendNotification(NotificationPayload payload) {
    NotificationChannel channel = notificationChannelFactory.getChannel(payload.channelType());
    channel.send(payload);
  }
}
