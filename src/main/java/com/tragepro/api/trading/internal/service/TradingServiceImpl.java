package com.tragepro.api.trading.internal.service;

import com.tragepro.api.trading.TradingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/** Service implementation executing trading events and logging order state transitions. */
@Slf4j
@Service
public class TradingServiceImpl implements TradingService {

  /**
   * Processes a trade execution event and logs trade details.
   *
   * @param event trading event payload containing eventId and message
   */
  @Override
  public void processTradingEvent(TradingEvent event) {
    log.info("Processing trading event: eventId={}, message={}", event.eventId(), event.message());
  }
}
