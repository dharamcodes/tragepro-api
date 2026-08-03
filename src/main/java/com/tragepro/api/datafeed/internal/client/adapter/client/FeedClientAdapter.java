package com.tragepro.api.datafeed.internal.client.adapter.client;

import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.FeedClientRequest;
import com.tragepro.api.datafeed.dto.FeedClientResponse;
import com.tragepro.api.datafeed.internal.client.FeedClient;
import com.tragepro.api.datafeed.internal.client.adapter.DataFeedAdapter;
import com.tragepro.api.datafeed.internal.client.mapper.FeedClientMapper;
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

  public List<CandleRequest> apiCallFallbackHistorical(FeedClientRequest request) {
    log.info("Calling fallback HistoricalFeed for securityId: {}", request.securityId());
    return processResponse(
        new FeedClientResponse(
            List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of()),
        request);
  }

  public List<CandleRequest> apiCallFallbackIntraday(FeedClientRequest request) {
    log.info("Calling fallback IntradayFeed for securityId: {}", request.securityId());
    return processResponse(
        new FeedClientResponse(
            List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of()),
        request);
  }
}
