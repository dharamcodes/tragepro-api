package com.tragepro.api.watchlist.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.candle.service.CandleService;
import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.service.WatchlistService;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class WatchlistMarketDataServiceImplTest {

    @Mock
    private WatchlistService watchlistService;

    @Mock
    private CandleService candleService;

    @InjectMocks
    private WatchlistMarketDataServiceImpl marketDataService;

    private WatchlistResponse watchlistResponse;
    private final String userId = "user123";
    private final String watchlistId = "wl123";

    @BeforeEach
    void setUp() {
        watchlistResponse = new WatchlistResponse(watchlistId, "My Watchlist", Set.of("BTCUSD", "ETHUSD"));
    }

    @Test
    void getMarketDataForWatchlist_NoSymbols() {
        when(watchlistService.getById(watchlistId, userId))
                .thenReturn(new WatchlistResponse(watchlistId, "Empty", Set.of()));

        List<WatchlistMarketDataResponse> result =
                marketDataService.getMarketDataForWatchlist(watchlistId, userId, null);
        assertTrue(result.isEmpty());
    }

    @Test
    void getMarketDataForWatchlist_Success_NoSort() {
        when(watchlistService.getById(watchlistId, userId)).thenReturn(watchlistResponse);

        CandleSummaryResponse btc = CandleSummaryResponse.builder()
                .symbolId("BTCUSD")
                .symbolName("Bitcoin")
                .open(100)
                .close(110)
                .volume(500)
                .build();
        CandleSummaryResponse eth = CandleSummaryResponse.builder()
                .symbolId("ETHUSD")
                .symbolName("Ethereum")
                .open(20)
                .close(25)
                .volume(1000)
                .build();

        when(candleService.getLatestForSymbols(any())).thenReturn(List.of(btc, eth));

        List<WatchlistMarketDataResponse> result =
                marketDataService.getMarketDataForWatchlist(watchlistId, userId, null);
        assertEquals(2, result.size());
        assertEquals("BTCUSD", result.get(0).symbolId());
        assertEquals(110, result.get(0).lastTradedPrice());
    }

    @Test
    void getMarketDataForWatchlist_Success_SortByVolumeDesc() {
        when(watchlistService.getById(watchlistId, userId)).thenReturn(watchlistResponse);

        CandleSummaryResponse btc = CandleSummaryResponse.builder()
                .symbolId("BTCUSD")
                .symbolName("Bitcoin")
                .open(100)
                .close(110)
                .volume(500)
                .build();
        CandleSummaryResponse eth = CandleSummaryResponse.builder()
                .symbolId("ETHUSD")
                .symbolName("Ethereum")
                .open(20)
                .close(25)
                .volume(1000)
                .build();

        when(candleService.getLatestForSymbols(any())).thenReturn(List.of(btc, eth));

        Sort sort = Sort.by(Sort.Direction.DESC, "volume");
        List<WatchlistMarketDataResponse> result =
                marketDataService.getMarketDataForWatchlist(watchlistId, userId, sort);

        assertEquals(2, result.size());
        assertEquals("ETHUSD", result.get(0).symbolId()); // 1000 > 500
        assertEquals("BTCUSD", result.get(1).symbolId());
    }

    @Test
    void getMarketDataForWatchlist_Success_SortByPriceAsc() {
        when(watchlistService.getById(watchlistId, userId)).thenReturn(watchlistResponse);

        CandleSummaryResponse btc = CandleSummaryResponse.builder()
                .symbolId("BTCUSD")
                .symbolName("Bitcoin")
                .open(100)
                .close(110)
                .volume(500)
                .build();
        CandleSummaryResponse eth = CandleSummaryResponse.builder()
                .symbolId("ETHUSD")
                .symbolName("Ethereum")
                .open(20)
                .close(25)
                .volume(1000)
                .build();

        when(candleService.getLatestForSymbols(any())).thenReturn(List.of(btc, eth));

        Sort sort = Sort.by(Sort.Direction.ASC, "price");
        List<WatchlistMarketDataResponse> result =
                marketDataService.getMarketDataForWatchlist(watchlistId, userId, sort);

        assertEquals(2, result.size());
        assertEquals("ETHUSD", result.get(0).symbolId()); // 25 < 110
        assertEquals("BTCUSD", result.get(1).symbolId());
    }
}
