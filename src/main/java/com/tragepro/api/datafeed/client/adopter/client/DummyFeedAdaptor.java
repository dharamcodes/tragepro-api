package com.tragepro.api.datafeed.client.adopter.client;

import com.tragepro.api.common.model.CandleDataModel;
import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.datafeed.client.adopter.DataFeedAdopter;
import com.tragepro.api.datafeed.client.mapper.FeedClientMapper;
import com.tragepro.api.datafeed.model.request.FeedClientRequest;
import com.tragepro.api.datafeed.model.response.FeedClientResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile(value = {"dev", "test", "local"})
@RequiredArgsConstructor
public class DummyFeedAdaptor implements DataFeedAdopter {

  private static final int ENTRIES = 10000;
  private static final double BASE_PRICE = 100.0;
  private static final long START_TIME_STAMP = 60_000L;
  private static final double VOLATILE_START = -2.0;
  private static final double VOLATILE_END = 2.0;
  private static final double PRICE_BOUND = 0.5;
  private static final long VOLUME_MIN = 100000;
  private static final long VOLUME_MAX = 1000000000;

  private final FeedClientMapper feedClientMapper;

  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackHistorical")
  @Override
  public List<CandleRequest> historicalDataAdaptor(FeedClientRequest request) {
    log.info("Fetching historical feed for securityId: {}", request.securityId());
    FeedClientResponse response = generateRandomResponse();
    return processResponse(response, request);
  }

  @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackIntraday")
  @Override
  public List<CandleRequest> intradayDataAdaptor(FeedClientRequest request) {
    log.info("Generating random 1000-entry intraday feed for securityId: {}", request.securityId());
    FeedClientResponse response = generateRandomResponse();
    return processResponse(response, request);
  }

  private FeedClientResponse generateRandomResponse() {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    double startPrice = BASE_PRICE + random.nextDouble(BASE_PRICE);
    long startTimestamp = System.currentTimeMillis() - (ENTRIES * START_TIME_STAMP);
    AtomicReference<Double> currentPrice = new AtomicReference<>(startPrice);
    AtomicLong currentTimestamp = new AtomicLong(startTimestamp);

    List<CandleDataModel> candles =
        IntStream.range(0, ENTRIES)
            .mapToObj(
                ind -> {
                  double open = currentPrice.get();
                  double change = random.nextDouble(VOLATILE_START, VOLATILE_END);
                  double close = open + change;
                  double high = Math.max(open, close) + random.nextDouble(PRICE_BOUND);
                  double low = Math.min(open, close) - random.nextDouble(PRICE_BOUND);
                  long volume = random.nextLong(VOLUME_MIN, VOLUME_MAX);
                  long timestamp = currentTimestamp.get();
                  currentPrice.set(close);
                  currentTimestamp.addAndGet(START_TIME_STAMP);

                  return CandleDataModel.builder()
                      .open(open)
                      .high(high)
                      .low(low)
                      .close(close)
                      .volume(volume)
                      .timestamp(timestamp)
                      .build();
                })
            .toList();

    return new FeedClientResponse(
        candles.stream().map(CandleDataModel::open).toList(),
        candles.stream().map(CandleDataModel::high).toList(),
        candles.stream().map(CandleDataModel::low).toList(),
        candles.stream().map(CandleDataModel::close).toList(),
        candles.stream().map(CandleDataModel::volume).toList(),
        candles.stream().map(CandleDataModel::timestamp).toList(),
        List.of());
  }

  private List<CandleRequest> processResponse(
      FeedClientResponse response, FeedClientRequest request) {
    return feedClientMapper.map(response, request);
  }

  private List<CandleRequest> apiCallFallbackHistorical(FeedClientRequest request) {
    log.info("Calling fallback HistoricalFeed for securityId: {}", request.securityId());
    return historicalDataAdaptor(request);
  }

  private List<CandleRequest> apiCallFallbackIntraday(FeedClientRequest request) {
    log.info("Calling fallback IntradayFeed for securityId: {}", request.securityId());
    return intradayDataAdaptor(request);
  }
}
