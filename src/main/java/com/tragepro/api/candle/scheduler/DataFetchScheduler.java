package com.tragepro.api.candle.scheduler;

import com.tragepro.api.candle.config.DataProviderProperties;
import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.service.CandleIngestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataFetchScheduler {

    private final CandleIngestionService candleIngestionService;
    private final DataProviderProperties properties;

    @Scheduled(cron = "${data-provider.fetch-cron:0 * * * * *}")
    public void fetchAndIngest() {
        CandleInterval interval = resolveInterval();
        log.info("Scheduled fetch triggered — interval: {}", interval.getValue());
        candleIngestionService.ingestAll(interval);
    }

    private CandleInterval resolveInterval() {
        String configured = properties.interval();
        if (configured == null || configured.isBlank()) {
            return CandleInterval.ONE_MINUTE;
        }
        for (CandleInterval ci : CandleInterval.values()) {
            if (ci.getValue().equalsIgnoreCase(configured)) {
                return ci;
            }
        }
        log.warn("Unrecognised data-provider.interval '{}' — falling back to ONE_MINUTE", configured);
        return CandleInterval.ONE_MINUTE;
    }
}
