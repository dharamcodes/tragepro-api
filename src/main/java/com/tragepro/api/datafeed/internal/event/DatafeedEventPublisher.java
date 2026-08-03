package com.tragepro.api.datafeed.internal.event;

import com.tragepro.api.datafeed.DatafeedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatafeedEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publish(DatafeedEvent event) {
    eventPublisher.publishEvent(event);
  }
}
