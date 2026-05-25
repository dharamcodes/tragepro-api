package com.tragepro.api.candle.scheduler;

import static org.mockito.Mockito.verify;

import com.tragepro.api.candle.config.DataProviderProperties;
import com.tragepro.api.candle.constant.CandleInterval;
import com.tragepro.api.candle.service.CandleIngestionService;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DataFetchSchedulerTest {

    @Mock
    private CandleIngestionService candleIngestionService;

    @Test
    void testFetchAndIngest_DefaultInterval() {
        DataProviderProperties props = new DataProviderProperties(Map.of(), null, null);
        DataFetchScheduler scheduler = new DataFetchScheduler(candleIngestionService, props);

        scheduler.fetchAndIngest();

        verify(candleIngestionService).ingestAll(CandleInterval.ONE_MINUTE);
    }

    @Test
    void testFetchAndIngest_BlankInterval() {
        DataProviderProperties props = new DataProviderProperties(Map.of(), null, "   ");
        DataFetchScheduler scheduler = new DataFetchScheduler(candleIngestionService, props);

        scheduler.fetchAndIngest();

        verify(candleIngestionService).ingestAll(CandleInterval.ONE_MINUTE);
    }

    @Test
    void testFetchAndIngest_UnrecognizedInterval() {
        DataProviderProperties props = new DataProviderProperties(Map.of(), null, "123m");
        DataFetchScheduler scheduler = new DataFetchScheduler(candleIngestionService, props);

        scheduler.fetchAndIngest();

        verify(candleIngestionService).ingestAll(CandleInterval.ONE_MINUTE); // fallback
    }

    @Test
    void testFetchAndIngest_ConfiguredInterval() {
        DataProviderProperties props = new DataProviderProperties(Map.of(), null, "4h");
        DataFetchScheduler scheduler = new DataFetchScheduler(candleIngestionService, props);

        scheduler.fetchAndIngest();

        verify(candleIngestionService).ingestAll(CandleInterval.FOUR_HOURS);
    }
}
