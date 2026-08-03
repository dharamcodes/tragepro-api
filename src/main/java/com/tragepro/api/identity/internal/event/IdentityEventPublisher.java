package com.tragepro.api.identity.internal.event;

import com.tragepro.api.identity.IdentityEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdentityEventPublisher {

  private final ApplicationEventPublisher eventPublisher;

  public void publish(IdentityEvent event) {
    eventPublisher.publishEvent(event);
  }
}
