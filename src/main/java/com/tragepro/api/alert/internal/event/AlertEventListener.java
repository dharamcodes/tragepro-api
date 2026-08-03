package com.tragepro.api.alert.internal.event;

import com.tragepro.api.alert.AlertEvent;
import com.tragepro.api.alert.internal.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlertEventListener {

  private final AlertService alertService;

  @Async
  @ApplicationModuleListener
  public void on(AlertEvent event) {
    alertService.processAlert(event);
  }
}
