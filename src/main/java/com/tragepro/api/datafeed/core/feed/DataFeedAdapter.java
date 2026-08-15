package com.tragepro.api.datafeed.core.feed;

import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import java.util.List;

public interface DataFeedAdapter {
  List<CandleRequest> historicalDataAdapter(FeedClientRequest request);

  List<CandleRequest> intradayDataAdapter(FeedClientRequest request);
}
