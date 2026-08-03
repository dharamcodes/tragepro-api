package com.tragepro.api.trading.internal.event;

import com.tragepro.api.trading.TradingEvent;
import com.tragepro.api.trading.internal.service.TradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradingEventListener {

  private final TradingService tradingService;

  @Async
  @ApplicationModuleListener
  public void on(TradingEvent event) {
    tradingService.processTradingEvent(event);
  }
}
