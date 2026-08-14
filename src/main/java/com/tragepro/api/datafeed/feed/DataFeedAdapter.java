package com.tragepro.api.datafeed.feed;

import com.tragepro.api.datafeed.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.request.FeedClientRequest;
import java.util.List;

public interface DataFeedAdapter {
  List<CandleRequest> historicalDataAdapter(FeedClientRequest request);

  List<CandleRequest> intradayDataAdapter(FeedClientRequest request);
}
