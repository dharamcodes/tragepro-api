package com.tragepro.api.data.scheduler;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.common.constant.ExchangeSegment;
import com.tragepro.api.common.constant.InstrumentType;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.data.model.SymbolData;
import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.SecurityResponse;
import com.tragepro.api.data.model.response.WatchListResponse;
import com.tragepro.api.data.scheduler.adopter.FeedDataHandler;
import com.tragepro.api.data.service.SecurityService;
import com.tragepro.api.data.service.WatchListService;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FeedDataSchedulerTest {

  @Mock private WatchListService watchlistService;
  @Mock private FeedDataHandler feedDataHandler;
  @Mock private SecurityService securityService;

  @InjectMocks private FeedDataScheduler feedDataScheduler;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(feedDataScheduler, "identifiers", "test-watchlist-id");
    ReflectionTestUtils.setField(feedDataScheduler, "interval", 90);
    ReflectionTestUtils.setField(feedDataScheduler, "enableHistorical", true);
    ReflectionTestUtils.setField(feedDataScheduler, "enableIntraday", true);
  }

  @Test
  void scheduleHistorical_Disabled() {
    ReflectionTestUtils.setField(feedDataScheduler, "enableHistorical", false);
    feedDataScheduler.scheduleHistorical();
    verify(watchlistService, never()).getById(anyString());
  }

  @Test
  void scheduleHistorical_WatchlistNotFound() {
    when(watchlistService.getById("test-watchlist-id")).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> feedDataScheduler.scheduleHistorical());
  }

  @Test
  void scheduleHistorical_Success() {
    WatchListResponse watchListResponse =
        new WatchListResponse(
            "test-watchlist-id", "Test Watchlist", "Desc", Set.of(new SymbolData("AAPL", "Apple")));
    when(watchlistService.getById("test-watchlist-id")).thenReturn(Optional.of(watchListResponse));

    SecurityResponse securityResponse =
        SecurityResponse.builder()
            .securityId(12345)
            .exchange(ExchangeSegment.NSE_EQ.getExchange())
            .instrument(InstrumentType.EQUITY.getValue())
            .build();
    when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(securityResponse);

    assertDoesNotThrow(() -> feedDataScheduler.scheduleHistorical());

    verify(feedDataHandler, times(1)).handleHistoricalData(any(FeedClientRequest.class));
  }

  @Test
  void scheduleIntraday_Disabled() {
    ReflectionTestUtils.setField(feedDataScheduler, "enableIntraday", false);
    feedDataScheduler.scheduleIntraday();
    verify(watchlistService, never()).getById(anyString());
  }

  @Test
  void scheduleIntraday_WatchlistNotFound() {
    when(watchlistService.getById("test-watchlist-id")).thenReturn(Optional.empty());
    assertThrows(AppException.class, () -> feedDataScheduler.scheduleIntraday());
  }

  @Test
  void scheduleIntraday_Success() {
    WatchListResponse watchListResponse =
        new WatchListResponse(
            "test-watchlist-id", "Test Watchlist", "Desc", Set.of(new SymbolData("AAPL", "Apple")));
    when(watchlistService.getById("test-watchlist-id")).thenReturn(Optional.of(watchListResponse));

    SecurityResponse securityResponse =
        SecurityResponse.builder()
            .securityId(12345)
            .exchange(ExchangeSegment.NSE_EQ.getExchange())
            .instrument(InstrumentType.EQUITY.getValue())
            .build();
    when(securityService.fetSecurityBySymbol("AAPL")).thenReturn(securityResponse);

    assertDoesNotThrow(() -> feedDataScheduler.scheduleIntraday());

    verify(feedDataHandler, times(1)).handleIntradayData(any(FeedClientRequest.class));
  }
}
