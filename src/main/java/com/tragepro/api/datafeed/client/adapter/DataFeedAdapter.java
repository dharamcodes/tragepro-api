package com.tragepro.api.datafeed.client.adapter;

import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.request.FeedClientRequest;
import java.util.List;

public interface DataFeedAdapter {
  List<CandleRequest> historicalDataAdapter(FeedClientRequest request);

  List<CandleRequest> intradayDataAdapter(FeedClientRequest request);
}
