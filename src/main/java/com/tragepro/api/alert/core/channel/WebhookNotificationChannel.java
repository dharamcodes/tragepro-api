package com.tragepro.api.alert.core.channel;

import com.tragepro.api.domain.alert.NotificationChannelType;
import com.tragepro.api.domain.alert.NotificationPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebhookNotificationChannel implements NotificationChannel {

  @Override
  public NotificationChannelType getChannelType() {
    return NotificationChannelType.WEBHOOK;
  }

  @Override
  public void send(NotificationPayload payload) {
    log.info("Sending Webhook payload to {}: {}", payload.recipient(), payload.message());
  }
}
