package com.tragepro.api.datafeed.core.client.adapter;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.datafeed.core.client.DataFeedAdapter;
import com.tragepro.api.datafeed.service.mapper.FeedClientMapper;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import com.tragepro.api.domain.datafeed.response.FeedClientResponse;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import java.util.ArrayList;
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
@Profile({"dev", "test", "local", "default"})
@RequiredArgsConstructor
public class DummyFeedAdapter implements DataFeedAdapter {

    private static final int ENTRIES = 1000; // 19800;
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
    public List<CandleRequest> historicalDataAdapter(FeedClientRequest request) {
        log.info("Fetching historical client for securityId: {}", request.securityId());
        FeedClientResponse response = generateRandomResponse();
        return processResponse(response, request);
    }

    @RateLimiter(name = "apiLimiter", fallbackMethod = "apiCallFallbackIntraday")
    @Override
    public List<CandleRequest> intradayDataAdapter(FeedClientRequest request) {
        log.info("Generating random {}-entry intraday client for securityId: {}", ENTRIES, request.securityId());
        FeedClientResponse response = generateRandomResponse();
        return processResponse(response, request);
    }

    private FeedClientResponse generateRandomResponse() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        double startPrice = BASE_PRICE + random.nextDouble(BASE_PRICE);
        long startTimestamp = System.currentTimeMillis() - (ENTRIES * START_TIME_STAMP);
        AtomicReference<Double> currentPrice = new AtomicReference<>(startPrice);
        AtomicLong currentTimestamp = new AtomicLong(startTimestamp);

        // Build all fields in a single pass to avoid 6 repeated stream iterations
        List<Double> opens = new ArrayList<>(ENTRIES);
        List<Double> highs = new ArrayList<>(ENTRIES);
        List<Double> lows = new ArrayList<>(ENTRIES);
        List<Double> closes = new ArrayList<>(ENTRIES);
        List<Long> volumes = new ArrayList<>(ENTRIES);
        List<Long> timestamps = new ArrayList<>(ENTRIES);

        IntStream.range(0, ENTRIES).forEach(ind -> {
            double open = currentPrice.get();
            double change = random.nextDouble(VOLATILE_START, VOLATILE_END);
            double close = open + change;
            double high = Math.max(open, close) + random.nextDouble(PRICE_BOUND);
            double low = Math.min(open, close) - random.nextDouble(PRICE_BOUND);
            long volume = random.nextLong(VOLUME_MIN, VOLUME_MAX);
            long timestamp = currentTimestamp.get();
            currentPrice.set(close);
            currentTimestamp.addAndGet(START_TIME_STAMP);

            opens.add(open);
            highs.add(high);
            lows.add(low);
            closes.add(close);
            volumes.add(volume);
            timestamps.add(timestamp);
        });

        return new FeedClientResponse(opens, highs, lows, closes, volumes, timestamps, List.of());
    }

    private List<CandleRequest> processResponse(FeedClientResponse response, FeedClientRequest request) {
        return feedClientMapper.map(response, request);
    }

    private List<CandleRequest> apiCallFallbackHistorical(FeedClientRequest request, Throwable throwable) {
        log.warn(
                "Rate limit triggered for historicalDataAdapter securityId: {}. Cause: {}",
                request.securityId(),
                throwable.getMessage());
        throw new AppException(ErrorType.TOO_MANY_REQUESTS);
    }

    private List<CandleRequest> apiCallFallbackIntraday(FeedClientRequest request, Throwable throwable) {
        log.warn(
                "Rate limit triggered for intradayDataAdapter securityId: {}. Cause: {}",
                request.securityId(),
                throwable.getMessage());
        throw new AppException(ErrorType.TOO_MANY_REQUESTS);
    }
}
