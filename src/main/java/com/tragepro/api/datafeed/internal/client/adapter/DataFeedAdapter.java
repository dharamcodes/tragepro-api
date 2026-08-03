package com.tragepro.api.datafeed.internal.client.adapter;

import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.FeedClientRequest;
import java.util.List;

public interface DataFeedAdapter {
  List<CandleRequest> historicalDataAdapter(FeedClientRequest request);

  List<CandleRequest> intradayDataAdapter(FeedClientRequest request);
}
