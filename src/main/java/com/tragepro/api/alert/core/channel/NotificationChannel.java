package com.tragepro.api.alert.core.channel;

import com.tragepro.api.domain.alert.NotificationChannelType;
import com.tragepro.api.domain.alert.NotificationPayload;

/** Strategy interface for alert channels (Email, Telegram, Webhook). */
public interface NotificationChannel {

  NotificationChannelType getChannelType();

  void send(NotificationPayload payload);
}
