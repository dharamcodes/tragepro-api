package com.tragepro.api.datafeed.internal.service;

import com.tragepro.api.datafeed.dto.SecurityResponse;

/** Domain service for looking up security metadata. */
public interface SecurityService {

  /**
   * Fetches security details by symbol.
   *
   * @param symbol market symbol
   * @return security response
   */
  SecurityResponse fetSecurityBySymbol(String symbol);
}
