package com.tragepro.api.datafeed.core.client;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;

import com.tragepro.api.datafeed.core.DatafeedInitializer;
import com.tragepro.api.datafeed.core.context.DatafeedContext;
import com.tragepro.api.datafeed.core.context.WatchlistContext;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.response.CandleResponse;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatafeedInitializerTest {

    @Mock
    private WatchListService watchListService;

    @Mock
    private WatchlistContext watchlistContext;

    @Mock
    private CandleService candleService;

    @Mock
    private DatafeedContext datafeedContext;

    @InjectMocks
    private DatafeedInitializer initializer;

    @Test
    void testRun_WithCandles() {
        SymbolDataModel symbol =
                SymbolDataModel.builder().symbol("AAPL").name("Apple").build();
        WatchListResponse watchlist =
                WatchListResponse.builder().name("WL1").stocks(Set.of(symbol)).build();

        CandleResponse candleMillis = CandleResponse.builder()
                .symbolData(symbol)
                .candleData(
                        CandleDataModel.builder().timestamp(1_700_000_000_000L).build())
                .build();

        CandleResponse candleEpochDays = CandleResponse.builder()
                .symbolData(symbol)
                .candleData(CandleDataModel.builder().timestamp(19000L).build())
                .build();

        org.mockito.Mockito.when(watchListService.getAll()).thenReturn(Set.of(watchlist));
        org.mockito.Mockito.when(candleService.getLatestCandlesBySymbols(Set.of("AAPL")))
                .thenReturn(Set.of(candleMillis, candleEpochDays));

        assertDoesNotThrow(() -> initializer.run());

        org.mockito.Mockito.verify(watchlistContext).addWatchlist("WL1", Set.of(symbol));
        org.mockito.Mockito.verify(datafeedContext, org.mockito.Mockito.times(2))
                .put(org.mockito.Mockito.eq(symbol), any());
    }
}
