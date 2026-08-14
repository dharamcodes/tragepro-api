package com.tragepro.api.alert.channel;

import com.tragepro.api.alert.model.NotificationChannelType;
import com.tragepro.api.alert.model.NotificationPayload;

/** Strategy interface for alert channels (Email, Telegram, Webhook). */
public interface NotificationChannel {

  NotificationChannelType getChannelType();

  void send(NotificationPayload payload);
}
