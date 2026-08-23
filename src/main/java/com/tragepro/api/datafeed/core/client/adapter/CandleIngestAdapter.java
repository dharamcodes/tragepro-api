package com.tragepro.api.datafeed.core.client.adapter;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.datafeed.core.client.factory.FeedAdapterFactory;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.FeedClientRequest;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.concurrent.Executors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CandleIngestAdapter {

    private final FeedAdapterFactory feedAdapterFactory;
    private final CandleService candleService;

    @Value("${data.fetch.max-concurrency:10}")
    private Integer concurrencyLevel;

    /**
     * Fetches historical intraday candles and ingests them concurrently into the candle datastore.
     *
     * @param security Security metadata (securityId, symbol, name)
     * @param stock Symbol model
     * @param daysBack Number of historical days to load
     * @return The latest candle date loaded, or today's date if no candles were retrieved
     */
    public LocalDate fetchAndIngest(SecurityResponse security, SymbolDataModel stock, int daysBack) {
        FeedClientRequest clientReq = buildFeedClientRequest(security, daysBack);
        List<CandleRequest> candles = feedAdapterFactory
                .get()
                .map(factory -> factory.intradayDataAdapter(clientReq))
                .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
        log.info("Retrieved {} candles from adapter for symbol: {}", candles.size(), stock.symbol());
        if (candles.isEmpty()) {
            return LocalDate.now();
        }
        SymbolDataModel enrichedSymbol = SymbolDataModel.builder()
                .symbol(stock.symbol())
                .name(stock.name())
                .build();
        persistCandlesConcurrently(candles, enrichedSymbol);
        return extractLatestCandleDate(candles);
    }

    private FeedClientRequest buildFeedClientRequest(SecurityResponse security, int daysBack) {
        LocalDate now = LocalDate.now();
        return FeedClientRequest.builder()
                .securityId(security.securityId())
                .instrument(security.symbol())
                .fromDate(now.minusDays(daysBack).toString())
                .toDate(now.toString())
                .build();
    }

    private void persistCandlesConcurrently(List<CandleRequest> candles, SymbolDataModel enrichedSymbol) {
        int poolSize = (concurrencyLevel != null && concurrencyLevel > 0) ? concurrencyLevel : 10;
        try (var executor =
                Executors.newFixedThreadPool(poolSize, Thread.ofVirtual().factory())) {
            candles.forEach(candle -> executor.submit(() -> {
                long timestamp = candle.candleData().timestamp();
                if (!candleService.isCandleExists(enrichedSymbol.name(), timestamp)) {
                    CandleRequest enriched = CandleRequest.builder()
                            .symbolData(enrichedSymbol)
                            .candleData(candle.candleData())
                            .build();
                    log.debug("Persisting candle for symbol: {} at timestamp: {}", enrichedSymbol.symbol(), timestamp);
                    candleService.create(enriched);
                }
            }));
        }
    }

    private LocalDate extractLatestCandleDate(List<CandleRequest> candles) {
        return candles.stream().mapToLong(candle -> candle.candleData().timestamp()).max().stream()
                .mapToObj(timestamp -> timestamp > 1_000_000_000_000L
                        ? Instant.ofEpochMilli(timestamp)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                        : LocalDate.ofEpochDay(timestamp))
                .findFirst()
                .orElse(LocalDate.now());
    }
}
