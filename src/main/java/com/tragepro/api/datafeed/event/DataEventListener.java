package com.tragepro.api.datafeed.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataEventListener {

  @ApplicationModuleListener
  public void on(DataEvent event) {
    log.info("Received DataEvent: {} - {}", event.eventId(), event);
  }
}
