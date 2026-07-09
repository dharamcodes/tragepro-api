package com.tragepro.api.datafeed.client.adopter;

import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.request.FeedClientRequest;
import java.util.List;

public interface DataFeedAdopter {
  List<CandleRequest> historicalDataAdaptor(FeedClientRequest request);

  List<CandleRequest> intradayDataAdaptor(FeedClientRequest request);
}
