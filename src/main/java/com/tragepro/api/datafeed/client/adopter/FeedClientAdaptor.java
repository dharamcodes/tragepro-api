package com.tragepro.api.datafeed.client.adopter;

import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.datafeed.client.FeedClient;
import com.tragepro.api.datafeed.client.mapper.FeedClientMapper;
import com.tragepro.api.datafeed.model.request.FeedClientRequest;
import com.tragepro.api.datafeed.model.response.FeedClientResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedClientAdaptor {

  private final FeedClient feedClient;
  private final FeedClientMapper feedClientMapper;

  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackHistorical")
  public List<CandleRequest> historicalDataAdaptor(FeedClientRequest request) {
    log.info("Fetching historical feed for securityId: {}", request.securityId());
    FeedClientResponse response = feedClient.getHistoricalFeed(request);
    return processResponse(response, request);
  }

  //
  //  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackIntraday")
  //  public List<CandleRequest> intradayDataAdaptor(FeedClientRequest request) {
  //    log.info("Fetching intraday feed for securityId: {}", request.securityId());
  //    FeedClientResponse response = feedClient.getIntradayFeed(request);
  //    return processResponse(response, request);
  //  }

  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackIntraday")
  public List<CandleRequest> intradayDataAdaptor(FeedClientRequest request) {
    log.info("Generating random 1000-entry intraday feed for securityId: {}", request.securityId());
    FeedClientResponse response = generateRandomResponse();
    return processResponse(response, request);
  }

  private FeedClientResponse generateRandomResponse() {
    int entries = 1000;
    List<Double> open = new ArrayList<>(entries);
    List<Double> high = new ArrayList<>(entries);
    List<Double> low = new ArrayList<>(entries);
    List<Double> close = new ArrayList<>(entries);
    List<Long> volume = new ArrayList<>(entries);
    List<Long> timestamp = new ArrayList<>(entries);
    List<Long> openInterest = new ArrayList<>(entries);

    ThreadLocalRandom random = ThreadLocalRandom.current();
    double currentPrice = 100.0 + random.nextDouble(100.0);
    // 1000 minutes back
    long currentTimestamp = System.currentTimeMillis() - (entries * 60_000L);

    for (int i = 0; i < entries; i++) {
      double op = currentPrice;
      double change = random.nextDouble(-2.0, 2.0);
      double cl = op + change;
      double hi = Math.max(op, cl) + random.nextDouble(0.5);
      double lo = Math.min(op, cl) - random.nextDouble(0.5);
      long vol = random.nextLong(100, 10000);
      long oi = random.nextLong(1000, 50000);

      open.add(op);
      high.add(hi);
      low.add(lo);
      close.add(cl);
      volume.add(vol);
      timestamp.add(currentTimestamp);
      openInterest.add(oi);

      currentPrice = cl;
      currentTimestamp += 60_000L;
    }

    return new FeedClientResponse(open, high, low, close, volume, timestamp, openInterest);
  }

  private List<CandleRequest> processResponse(
      FeedClientResponse response, FeedClientRequest request) {
    return feedClientMapper.map(response, request);
  }

  private List<CandleRequest> apiCallFallbackHistorical(FeedClientRequest request)
      throws InterruptedException {
    log.info("Calling fallback HistoricalFeed for securityId: {}", request.securityId());
    Thread.sleep(10000);
    return historicalDataAdaptor(request);
  }

  private List<CandleRequest> apiCallFallbackIntraday(FeedClientRequest request)
      throws InterruptedException {
    log.info("Calling fallback IntradayFeed for securityId: {}", request.securityId());
    Thread.sleep(10000);
    return intradayDataAdaptor(request);
  }
}
