package com.tragepro.api.datafeed.internal.event;

import com.tragepro.api.datafeed.DatafeedEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class DatafeedEventListener {

  @Async
  @ApplicationModuleListener
  public void on(DatafeedEvent event) {}
}
