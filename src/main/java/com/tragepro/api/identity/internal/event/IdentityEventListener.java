package com.tragepro.api.identity.internal.event;

import com.tragepro.api.identity.IdentityEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class IdentityEventListener {

  @Async
  @ApplicationModuleListener
  public void on(IdentityEvent event) {}
}
