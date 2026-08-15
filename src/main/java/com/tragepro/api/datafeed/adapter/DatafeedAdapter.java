package com.tragepro.api.datafeed.adapter;

import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;

public interface DatafeedAdapter {
  LoadCandleResponse loadData(LoadCandleRequest request);
}
