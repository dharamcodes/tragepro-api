package com.tragepro.api.datafeed.adapter.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.datafeed.service.DatafeedService;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import com.tragepro.api.domain.datafeed.response.CandleResponse;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class MarketDataAdapterTest {

  @Mock private DatafeedService datafeedService;
  @Mock private CandleService candleService;
  @Mock private SecurityService securityService;
  @Mock private WatchListService watchListService;

  private DatafeedAdapterImpl datafeedAdapter;
  private CandleAdapterImpl candleAdapter;
  private SecurityAdapterImpl securityAdapter;
  private WatchListAdapterImpl watchListAdapter;

  @BeforeEach
  void setUp() {
    datafeedAdapter = new DatafeedAdapterImpl(datafeedService);
    candleAdapter = new CandleAdapterImpl(candleService);
    securityAdapter = new SecurityAdapterImpl(securityService);
    watchListAdapter = new WatchListAdapterImpl(watchListService);
  }

  @Test
  void testLoadData() {
    LoadCandleRequest request = new LoadCandleRequest("AAPL", 5);
    LoadCandleResponse expectedResponse =
        LoadCandleResponse.builder().watchList("AAPL").message("Loaded").build();
    when(datafeedService.loadData(request)).thenReturn(expectedResponse);

    LoadCandleResponse response = datafeedAdapter.loadData(request);

    assertNotNull(response);
    assertEquals("AAPL", response.watchList());
    verify(datafeedService).loadData(request);
  }

  @Test
  void testCandleAdapterMethods() {
    CandleRequest candleRequest = CandleRequest.builder().build();
    CandleResponse candleResponse = CandleResponse.builder().build();
    Pageable pageable = PageRequest.of(0, 10);
    Page<CandleResponse> page = new PageImpl<>(List.of(candleResponse));

    when(candleService.create(candleRequest)).thenReturn(candleResponse);
    assertEquals(candleResponse, candleAdapter.create(candleRequest));

    when(candleService.getById("c-1")).thenReturn(Optional.of(candleResponse));
    assertTrue(candleAdapter.getById("c-1").isPresent());

    when(candleService.getAll(pageable)).thenReturn(page);
    assertEquals(page, candleAdapter.getAll(pageable));

    when(candleService.getAll()).thenReturn(Set.of(candleResponse));
    assertEquals(1, candleAdapter.getAll().size());

    when(candleService.update("c-1", candleRequest)).thenReturn(candleResponse);
    assertEquals(candleResponse, candleAdapter.update("c-1", candleRequest));

    candleAdapter.delete("c-1");
    verify(candleService).delete("c-1");

    when(candleService.isCandleExists("BTC", 12345L)).thenReturn(true);
    assertTrue(candleAdapter.isCandleExists("BTC", 12345L));

    when(candleService.getCandlesBySymbolAndDaysBack("BTC", 7)).thenReturn(List.of(candleResponse));
    assertEquals(1, candleAdapter.getCandlesBySymbolAndDaysBack("BTC", 7).size());

    when(candleService.getLatestCandlesBySymbols(Set.of("BTC"))).thenReturn(Set.of(candleResponse));
    assertEquals(1, candleAdapter.getLatestCandlesBySymbols(Set.of("BTC")).size());
  }

  @Test
  void testSecurityAdapterMethods() {
    SecurityResponse securityResponse = SecurityResponse.builder().symbol("AAPL").build();
    when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(securityResponse);

    assertEquals(securityResponse, securityAdapter.fetSecurityBySymbol("AAPL"));
    verify(securityService).fetSecurityBySymbol("AAPL");
  }

  @Test
  void testWatchListAdapterMethods() {
    WatchListRequest request = WatchListRequest.builder().name("Tech").build();
    WatchListResponse response = WatchListResponse.builder().name("Tech").build();
    Pageable pageable = PageRequest.of(0, 10);
    Page<WatchListResponse> page = new PageImpl<>(List.of(response));

    when(watchListService.create(request)).thenReturn(response);
    assertEquals(response, watchListAdapter.create(request));

    when(watchListService.getById("w-1")).thenReturn(Optional.of(response));
    assertTrue(watchListAdapter.getById("w-1").isPresent());

    when(watchListService.getAll(pageable)).thenReturn(page);
    assertEquals(page, watchListAdapter.getAll(pageable));

    when(watchListService.getAll()).thenReturn(Set.of(response));
    assertEquals(1, watchListAdapter.getAll().size());

    when(watchListService.update("w-1", request)).thenReturn(response);
    assertEquals(response, watchListAdapter.update("w-1", request));

    watchListAdapter.delete("w-1");
    verify(watchListService).delete("w-1");

    when(watchListService.patch("w-1", request)).thenReturn(response);
    assertEquals(response, watchListAdapter.patch("w-1", request));
  }
}
