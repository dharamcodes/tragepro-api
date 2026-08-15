package com.tragepro.api.datafeed.service;

import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;

public interface DatafeedService {
  LoadCandleResponse loadData(LoadCandleRequest request);
}
