package com.tragepro.api.data.client.adopter;

import com.tragepro.api.data.client.FeedClient;
import com.tragepro.api.data.client.mapper.FeedClientMapper;
import com.tragepro.api.data.model.request.CandleRequest;
import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.FeedClientResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedClientAdaptor {

  private final FeedClient feedClient;
  private final FeedClientMapper feedClientMapper;

  public List<CandleRequest> historicalDataAdaptor(FeedClientRequest request) {
    log.info("Fetching historical feed for securityId: {}", request.securityId());
    FeedClientResponse response = feedClient.getHistoricalFeed(request);
    return processResponse(response, request);
  }

  public List<CandleRequest> intradayDataAdaptor(FeedClientRequest request) {
    log.info("Fetching intraday feed for securityId: {}", request.securityId());
    FeedClientResponse response = feedClient.getIntradayFeed(request);
    return processResponse(response, request);
  }

  private List<CandleRequest> processResponse(
      FeedClientResponse response, FeedClientRequest request) {
    return feedClientMapper.map(response, request);
  }
}
