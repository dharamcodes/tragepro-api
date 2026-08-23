package com.tragepro.api.datafeed.core.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.datafeed.core.client.adapter.CandleIngestAdapter;
import com.tragepro.api.datafeed.core.client.factory.FeedAdapterFactory;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CandleIngestAdapterTest {

    @Mock
    private FeedAdapterFactory feedAdapterFactory;

    @Mock
    private DataFeedAdapter dataFeedAdapter;

    @Mock
    private CandleService candleService;

    private CandleIngestAdapter processor;

    @BeforeEach
    void setUp() {
        lenient().when(feedAdapterFactory.get()).thenReturn(Optional.of(dataFeedAdapter));
        processor = new CandleIngestAdapter(feedAdapterFactory, candleService);
    }

    @Test
    void testFetchAndIngest_Success_NewCandle() {
        SecurityResponse security = SecurityResponse.builder()
                .securityId(101)
                .symbol("AAPL")
                .name("Apple Inc.")
                .build();
        SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");

        CandleRequest mockCandle = CandleRequest.builder()
                .candleData(new CandleDataModel(1609459200000L, 100.0, 110.0, 90.0, 105.0, 1000L))
                .build();

        when(dataFeedAdapter.intradayDataAdapter(any())).thenReturn(List.of(mockCandle));
        when(candleService.isCandleExists("Apple Inc.", 1609459200000L)).thenReturn(false);

        LocalDate result = processor.fetchAndIngest(security, stock, 5);

        assertNotNull(result);
        verify(candleService).create(any());
    }

    @Test
    void testFetchAndIngest_CandleAlreadyExists_DoesNotDuplicate() {
        SecurityResponse security = SecurityResponse.builder()
                .securityId(101)
                .symbol("AAPL")
                .name("Apple Inc.")
                .build();
        SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");

        CandleRequest mockCandle = CandleRequest.builder()
                .candleData(new CandleDataModel(100L, 100.0, 110.0, 90.0, 105.0, 1000L))
                .build();

        when(dataFeedAdapter.intradayDataAdapter(any())).thenReturn(List.of(mockCandle));
        when(candleService.isCandleExists("Apple Inc.", 100L)).thenReturn(true);

        LocalDate result = processor.fetchAndIngest(security, stock, 5);

        assertNotNull(result);
        verify(candleService, never()).create(any());
    }

    @Test
    void testFetchAndIngest_EmptyCandles_ReturnsToday() {
        SecurityResponse security = SecurityResponse.builder()
                .securityId(101)
                .symbol("AAPL")
                .name("Apple Inc.")
                .build();
        SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");

        when(dataFeedAdapter.intradayDataAdapter(any())).thenReturn(List.of());

        LocalDate result = processor.fetchAndIngest(security, stock, 5);

        assertNotNull(result);
        assertEquals(LocalDate.now(), result);
        verify(candleService, never()).create(any());
    }
}
