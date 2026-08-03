package com.tragepro.api.alert.internal.event;

import com.tragepro.api.alert.AlertEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publish(AlertEvent event) {
    eventPublisher.publishEvent(event);
  }
}
