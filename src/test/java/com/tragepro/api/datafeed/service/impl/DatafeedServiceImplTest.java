package com.tragepro.api.datafeed.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.datafeed.core.client.adapter.CandleIngestAdapter;
import com.tragepro.api.datafeed.core.context.DatafeedContext;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.constant.DatafeedState;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatafeedServiceImplTest {

    @Mock
    private WatchListService watchListService;

    @Mock
    private SecurityService securityService;

    @Mock
    private CandleIngestAdapter candleIngestAdapter;

    @Mock
    private DatafeedContext datafeedContext;

    private DatafeedServiceImpl datafeedService;

    @BeforeEach
    void setUp() {
        datafeedService =
                new DatafeedServiceImpl(watchListService, securityService, candleIngestAdapter, datafeedContext);
    }

    @Test
    void testLoadData_Success() {
        LoadCandleRequest request = new LoadCandleRequest("MyWatchlist", 5);
        SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");
        WatchListResponse watchlist = WatchListResponse.builder()
                .name("MyWatchlist")
                .stocks(Set.of(stock))
                .build();

        SecurityResponse security = SecurityResponse.builder()
                .securityId(1001)
                .symbol("AAPL")
                .name("Apple Inc.")
                .build();

        when(watchListService.getAll()).thenReturn(Set.of(watchlist));
        when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(security);
        when(candleIngestAdapter.fetchAndIngest(security, stock, 5)).thenReturn(LocalDate.now());

        LoadCandleResponse response = datafeedService.loadData(request);

        assertNotNull(response);
        assertEquals("MyWatchlist", response.watchList());
        assertEquals("Data load initiated successfully", response.message());

        verify(datafeedContext, timeout(3000)).transitionTo(stock, DatafeedState.PROCESSING);
        verify(datafeedContext, timeout(3000)).transitionTo(eq(stock), eq(DatafeedState.COMPLETED), any());
        verify(datafeedContext, timeout(3000)).transitionTo(stock, DatafeedState.INITIALIZED);
    }

    @Test
    void testLoadData_NullRequest_ThrowsException() {
        assertThrows(AppException.class, () -> datafeedService.loadData(null));
        assertThrows(AppException.class, () -> datafeedService.loadData(new LoadCandleRequest(null, 5)));
    }

    @Test
    void testLoadData_WatchlistNotFound() {
        LoadCandleRequest request = new LoadCandleRequest("NonExistent", 5);
        when(watchListService.getAll()).thenReturn(Set.of());

        assertThrows(AppException.class, () -> datafeedService.loadData(request));
    }

    @Test
    void testLoadData_EmptyStocks() {
        LoadCandleRequest request = new LoadCandleRequest("EmptyWatchlist", 5);
        WatchListResponse watchlist = WatchListResponse.builder()
                .name("EmptyWatchlist")
                .stocks(Set.of())
                .build();

        when(watchListService.getAll()).thenReturn(Set.of(watchlist));

        LoadCandleResponse response = datafeedService.loadData(request);

        assertNotNull(response);
        assertEquals("EmptyWatchlist", response.watchList());
        assertEquals("No symbols found in watchlist to process", response.message());
    }

    @Test
    void testLoadData_NullStocks() {
        LoadCandleRequest request = new LoadCandleRequest("NullStocksWatchlist", 5);
        WatchListResponse watchlist = WatchListResponse.builder()
                .name("NullStocksWatchlist")
                .stocks(null)
                .build();

        when(watchListService.getAll()).thenReturn(Set.of(watchlist));

        LoadCandleResponse response = datafeedService.loadData(request);

        assertNotNull(response);
        assertEquals("NullStocksWatchlist", response.watchList());
        assertEquals("No symbols found in watchlist to process", response.message());
    }

    @Test
    void testLoadData_SecurityNotFound_SkipsStock() {
        LoadCandleRequest request = new LoadCandleRequest("MyWatchlist", 5);
        SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");
        WatchListResponse watchlist = WatchListResponse.builder()
                .name("MyWatchlist")
                .stocks(Set.of(stock))
                .build();

        when(watchListService.getAll()).thenReturn(Set.of(watchlist));
        when(securityService.fetSecurityBySymbol("AAPL")).thenThrow(new RuntimeException("Not Found"));

        LoadCandleResponse response = datafeedService.loadData(request);

        assertNotNull(response);
        verify(securityService, timeout(3000)).fetSecurityBySymbol("AAPL");
        verify(candleIngestAdapter, never()).fetchAndIngest(any(), any(), anyInt());
    }

    @Test
    void testLoadData_IngestionThrowsException_RevertsState() {
        LoadCandleRequest request = new LoadCandleRequest("MyWatchlist", 5);
        SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");
        WatchListResponse watchlist = WatchListResponse.builder()
                .name("MyWatchlist")
                .stocks(Set.of(stock))
                .build();

        SecurityResponse security = SecurityResponse.builder()
                .securityId(1001)
                .symbol("AAPL")
                .name("Apple Inc.")
                .build();

        when(watchListService.getAll()).thenReturn(Set.of(watchlist));
        when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(security);
        when(candleIngestAdapter.fetchAndIngest(security, stock, 5)).thenThrow(new RuntimeException("Network Error"));

        LoadCandleResponse response = datafeedService.loadData(request);

        assertNotNull(response);
        verify(datafeedContext, timeout(3000)).transitionTo(stock, DatafeedState.PROCESSING);
        verify(datafeedContext, timeout(3000)).transitionTo(stock, DatafeedState.INITIALIZED);
    }
}
