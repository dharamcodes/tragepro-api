package com.tragepro.api.datafeed.internal.service;

import com.tragepro.api.datafeed.dto.LoadCandleRequest;
import com.tragepro.api.datafeed.dto.LoadCandleResponse;

/** Domain service for loading external market datafeed feeds. */
public interface DatafeedService {

  /**
   * Executes datafeed ingestion for requested watchlists or symbols.
   *
   * @param request load request payload
   * @return load response status
   */
  LoadCandleResponse loadData(LoadCandleRequest request);
}
