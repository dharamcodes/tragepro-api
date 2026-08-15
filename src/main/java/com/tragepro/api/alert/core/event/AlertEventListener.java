package com.tragepro.api.alert.core.event;

import com.tragepro.api.alert.core.channel.NotificationChannelFactory;
import com.tragepro.api.domain.alert.NotificationChannelType;
import com.tragepro.api.domain.alert.NotificationPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlertEventListener {

  private final NotificationChannelFactory notificationChannelFactory;

  @ApplicationModuleListener
  public void on(AlertEvent event) {
    log.info("Received AlertEvent: {} - {}", event.eventId(), event.message());
    try {
      NotificationPayload payload =
          NotificationPayload.builder()
              .recipient("system-alerts@tragepro.com")
              .subject("Alert Notification")
              .message(event.message())
              .channelType(NotificationChannelType.EMAIL)
              .build();
      notificationChannelFactory.getChannel(payload.channelType()).send(payload);
    } catch (Exception e) {
      log.error("Failed to process alert for AlertEvent: {}", event.eventId(), e);
    }
  }
}
