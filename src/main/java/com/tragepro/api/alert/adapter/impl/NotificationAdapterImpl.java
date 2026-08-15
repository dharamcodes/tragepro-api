package com.tragepro.api.alert.adapter.impl;

import com.tragepro.api.alert.adapter.NotificationAdapter;
import com.tragepro.api.alert.core.channel.NotificationChannel;
import com.tragepro.api.alert.core.channel.NotificationChannelFactory;
import com.tragepro.api.domain.alert.NotificationPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationAdapterImpl implements NotificationAdapter {

  private final NotificationChannelFactory notificationChannelFactory;

  @Override
  public void sendNotification(NotificationPayload payload) {
    NotificationChannel channel = notificationChannelFactory.getChannel(payload.channelType());
    channel.send(payload);
  }
}
