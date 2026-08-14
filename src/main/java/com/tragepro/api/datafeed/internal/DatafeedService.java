package com.tragepro.api.datafeed.internal;

import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.response.LoadCandleResponse;

interface DatafeedService {
  LoadCandleResponse loadData(LoadCandleRequest request);
}
