package com.tragepro.api.trading.internal.service;

import com.tragepro.api.trading.TradingEvent;

/** Domain service managing trading execution events and order processing. */
public interface TradingService {

  /**
   * Processes a trade execution event and logs trade details.
   *
   * @param event trading event payload containing tradeId and message
   */
  void processTradingEvent(TradingEvent event);
}
