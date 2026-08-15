package com.tragepro.api.datafeed.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.datafeed.core.context.DatafeedContext;
import com.tragepro.api.datafeed.core.feed.DataFeedAdapter;
import com.tragepro.api.datafeed.core.feed.FeedAdapterFactory;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.CandleDataModel;
import com.tragepro.api.domain.datafeed.DatafeedModel;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DatafeedServiceImplTest {

  @Mock private WatchListService watchListService;
  @Mock private SecurityService securityService;
  @Mock private CandleService candleService;
  @Mock private FeedAdapterFactory feedAdapterFactory;
  @Mock private DataFeedAdapter dataFeedAdapter;
  @Mock private DatafeedContext datafeedContext;

  private Executor executor = Runnable::run; // Sync executor for tests

  private DatafeedServiceImpl datafeedService;

  @BeforeEach
  void setUp() {
    lenient().when(feedAdapterFactory.get()).thenReturn(dataFeedAdapter);
    datafeedService =
        new DatafeedServiceImpl(
            watchListService,
            securityService,
            candleService,
            feedAdapterFactory,
            datafeedContext,
            executor);
  }

  @Test
  void testLoadData_Success() {
    LoadCandleRequest request = new LoadCandleRequest("MyWatchlist", 5);
    SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");
    WatchListResponse watchlist =
        WatchListResponse.builder().name("MyWatchlist").stocks(Set.of(stock)).build();

    SecurityResponse security =
        SecurityResponse.builder().securityId(1001).symbol("AAPL").name("Apple Inc.").build();

    CandleRequest mockCandle =
        CandleRequest.builder()
            .candleData(new CandleDataModel(1609459200000L, 100.0, 110.0, 90.0, 105.0, 1000L))
            .build();

    when(watchListService.getAll()).thenReturn(Set.of(watchlist));
    when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(security);
    when(dataFeedAdapter.intradayDataAdapter(any())).thenReturn(List.of(mockCandle));
    when(candleService.isCandleExists(anyString(), anyLong())).thenReturn(false);

    LoadCandleResponse response = datafeedService.loadData(request);

    assertNotNull(response);
    assertEquals("MyWatchlist", response.watchList());
    assertEquals("Data load initiated successfully", response.message());

    verify(candleService).create(any());
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
    WatchListResponse watchlist =
        WatchListResponse.builder().name("EmptyWatchlist").stocks(Set.of()).build();

    when(watchListService.getAll()).thenReturn(Set.of(watchlist));

    LoadCandleResponse response = datafeedService.loadData(request);

    assertNotNull(response);
    assertEquals("EmptyWatchlist", response.watchList());
    assertEquals("No symbols found in watchlist to process", response.message());
  }

  @Test
  void testLoadData_SecurityNotFound_SkipsStock() {
    LoadCandleRequest request = new LoadCandleRequest("MyWatchlist", 5);
    SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");
    WatchListResponse watchlist =
        WatchListResponse.builder().name("MyWatchlist").stocks(Set.of(stock)).build();

    when(watchListService.getAll()).thenReturn(Set.of(watchlist));
    when(securityService.fetSecurityBySymbol("AAPL")).thenThrow(new RuntimeException("Not Found"));

    LoadCandleResponse response = datafeedService.loadData(request);

    assertNotNull(response);
    verify(dataFeedAdapter, never()).intradayDataAdapter(any());
  }

  @Test
  void testLoadData_AdapterThrowsException_RevertsState() {
    LoadCandleRequest request = new LoadCandleRequest("MyWatchlist", 5);
    SymbolDataModel stock = new SymbolDataModel("AAPL", "Apple Inc.");
    WatchListResponse watchlist =
        WatchListResponse.builder().name("MyWatchlist").stocks(Set.of(stock)).build();

    SecurityResponse security =
        SecurityResponse.builder().securityId(1001).symbol("AAPL").name("Apple Inc.").build();

    when(watchListService.getAll()).thenReturn(Set.of(watchlist));
    when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(security);
    when(datafeedContext.get(stock)).thenReturn(DatafeedModel.builder().build());
    when(dataFeedAdapter.intradayDataAdapter(any()))
        .thenThrow(new RuntimeException("Network Error"));

    LoadCandleResponse response = datafeedService.loadData(request);

    assertNotNull(response);
    verify(datafeedContext, atLeastOnce()).updateStatus(eq(stock), any());
  }
}
