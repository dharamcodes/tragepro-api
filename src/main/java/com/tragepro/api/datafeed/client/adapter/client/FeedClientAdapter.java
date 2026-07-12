package com.tragepro.api.datafeed.client.adapter.client;

import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.datafeed.client.FeedClient;
import com.tragepro.api.datafeed.client.adapter.DataFeedAdapter;
import com.tragepro.api.datafeed.client.mapper.FeedClientMapper;
import com.tragepro.api.datafeed.model.request.FeedClientRequest;
import com.tragepro.api.datafeed.model.response.FeedClientResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(value = {"ppe", "prod"})
@RequiredArgsConstructor
public class FeedClientAdapter implements DataFeedAdapter {

  private final FeedClient feedClient;
  private final FeedClientMapper feedClientMapper;

  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackHistorical")
  @Override
  public List<CandleRequest> historicalDataAdapter(FeedClientRequest request) {
    log.info("Fetching historical feed for securityId: {}", request.securityId());
    FeedClientResponse response = feedClient.getHistoricalFeed(request);
    return processResponse(response, request);
  }

  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackIntraday")
  @Override
  public List<CandleRequest> intradayDataAdapter(FeedClientRequest request) {
    log.info("Fetching intraday feed for securityId: {}", request.securityId());
    FeedClientResponse response = feedClient.getIntradayFeed(request);
    return processResponse(response, request);
  }

  private List<CandleRequest> processResponse(
      FeedClientResponse response, FeedClientRequest request) {
    return feedClientMapper.map(response, request);
  }

  private List<CandleRequest> apiCallFallbackHistorical(FeedClientRequest request)
      throws InterruptedException {
    log.info("Calling fallback HistoricalFeed for securityId: {}", request.securityId());
    Thread.sleep(10000);
    return historicalDataAdapter(request);
  }

  private List<CandleRequest> apiCallFallbackIntraday(FeedClientRequest request)
      throws InterruptedException {
    log.info("Calling fallback IntradayFeed for securityId: {}", request.securityId());
    Thread.sleep(10000);
    return intradayDataAdapter(request);
  }
}
