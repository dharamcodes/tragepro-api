package com.tragepro.api.datafeed.core.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataEventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public void publish(DataEvent event) {
    log.info("Publishing DataEvent: {}", event.eventId());
    applicationEventPublisher.publishEvent(event);
  }
}
