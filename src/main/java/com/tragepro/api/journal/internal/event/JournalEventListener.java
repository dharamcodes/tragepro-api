package com.tragepro.api.journal.internal.event;

import com.tragepro.api.journal.JournalEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class JournalEventListener {

  @Async
  @ApplicationModuleListener
  public void on(JournalEvent event) {}
}
