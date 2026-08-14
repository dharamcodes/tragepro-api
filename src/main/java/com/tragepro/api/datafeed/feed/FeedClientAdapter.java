package com.tragepro.api.datafeed.feed;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.datafeed.mapper.FeedClientMapper;
import com.tragepro.api.datafeed.model.request.CandleRequest;
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
@Profile("prod")
@RequiredArgsConstructor
class FeedClientAdapter implements DataFeedAdapter {

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

  private List<CandleRequest> apiCallFallbackHistorical(
      FeedClientRequest request, Throwable throwable) {
    log.warn(
        "Rate limit triggered for historicalDataAdapter securityId: {}. Cause: {}",
        request.securityId(),
        throwable.getMessage());
    throw new AppException(ErrorType.TOO_MANY_REQUESTS);
  }

  private List<CandleRequest> apiCallFallbackIntraday(
      FeedClientRequest request, Throwable throwable) {
    log.warn(
        "Rate limit triggered for intradayDataAdapter securityId: {}. Cause: {}",
        request.securityId(),
        throwable.getMessage());
    throw new AppException(ErrorType.TOO_MANY_REQUESTS);
  }
}
