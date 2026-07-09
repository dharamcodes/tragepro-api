package com.tragepro.api.datafeed.service;

import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.response.LoadCandleResponse;

public interface DatafeedService {
  LoadCandleResponse loadData(LoadCandleRequest request);
}
