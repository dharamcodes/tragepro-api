package com.tragepro.api.trading.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TradingEventListener {

  @ApplicationModuleListener
  public void on(TradingEvent event) {
    log.info("Received TradingEvent: {} - {}", event.eventId(), event.message());
  }
}
