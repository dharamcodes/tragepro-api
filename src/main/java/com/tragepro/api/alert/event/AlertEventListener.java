package com.tragepro.api.alert.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AlertEventListener {

  @ApplicationModuleListener
  public void on(AlertEvent event) {
    log.info("Received AlertEvent: {} - {}", event.eventId(), event.message());
  }
}
