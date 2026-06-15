package com.tragepro.api.data.scheduler.adopter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.data.client.adopter.FeedClientAdaptor;
import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
import com.tragepro.api.data.model.request.CandleRequest;
import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.CandleResponse;
import com.tragepro.api.data.service.CandleService;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeedDataHandlerTest {

  @Mock private FeedClientAdaptor feedClientAdaptor;

  @Mock private CandleService candleService;

  @InjectMocks private FeedDataHandler feedDataHandler;

  private FeedClientRequest feedClientRequest;
  private CandleRequest validCandleRequest;

  @BeforeEach
  void setUp() {
    feedClientRequest =
        new FeedClientRequest(
            1, "TEST_EXCHANGE", "TEST_INSTRUMENT", "TEST_SYMBOL", 1, true, "fromDate", "toDate");
    validCandleRequest =
        CandleRequest.builder()
            .symbolData(new SymbolData("AAPL", "Apple Inc."))
            .candleData(new CandleData(1609459200000L, 100.0, 110.0, 90.0, 105.0, 1000.0))
            .build();
  }

  @Test
  void handleHistoricalData_Success() {
    when(feedClientAdaptor.historicalDataAdaptor(any())).thenReturn(List.of(validCandleRequest));
    when(candleService.isCandleExists("Apple Inc.", 1609459200000L)).thenReturn(false);
    when(candleService.create(any())).thenReturn(CandleResponse.builder().build());

    assertDoesNotThrow(() -> feedDataHandler.handleHistoricalData(feedClientRequest));

    verify(candleService, times(1)).create(any());
  }

  @Test
  void handleHistoricalData_NoDataFound() {
    when(feedClientAdaptor.historicalDataAdaptor(any())).thenReturn(Collections.emptyList());

    assertThrows(AppException.class, () -> feedDataHandler.handleHistoricalData(feedClientRequest));
  }

  @Test
  void handleIntradayData_Success() {
    when(feedClientAdaptor.intradayDataAdaptor(any())).thenReturn(List.of(validCandleRequest));
    when(candleService.isCandleExists("Apple Inc.", 1609459200000L)).thenReturn(false);
    when(candleService.create(any())).thenReturn(CandleResponse.builder().build());

    assertDoesNotThrow(() -> feedDataHandler.handleIntradayData(feedClientRequest));

    verify(candleService, times(1)).create(any());
  }

  @Test
  void handleIntradayData_NoDataFound() {
    when(feedClientAdaptor.intradayDataAdaptor(any())).thenReturn(Collections.emptyList());

    assertThrows(AppException.class, () -> feedDataHandler.handleIntradayData(feedClientRequest));
  }

  @Test
  void processCandle_AlreadyExists() {
    when(feedClientAdaptor.historicalDataAdaptor(any())).thenReturn(List.of(validCandleRequest));
    when(candleService.isCandleExists("Apple Inc.", 1609459200000L)).thenReturn(true);

    assertDoesNotThrow(() -> feedDataHandler.handleHistoricalData(feedClientRequest));

    verify(candleService, never()).create(any());
  }

  @Test
  void processCandle_InternalError() {
    when(feedClientAdaptor.historicalDataAdaptor(any())).thenReturn(List.of(validCandleRequest));
    when(candleService.isCandleExists("Apple Inc.", 1609459200000L)).thenReturn(false);
    when(candleService.create(any())).thenReturn(null);

    assertThrows(AppException.class, () -> feedDataHandler.handleHistoricalData(feedClientRequest));
  }

  @Test
  void processCandle_InvalidCandleScenarios() {
    // null symbolData
    CandleRequest invalid1 =
        CandleRequest.builder()
            .candleData(new CandleData(1609459200000L, 100.0, 110.0, 90.0, 105.0, 1000.0))
            .build();
    // blank symbol name
    CandleRequest invalid2 =
        CandleRequest.builder()
            .symbolData(new SymbolData("AAPL", ""))
            .candleData(new CandleData(1609459200000L, 100.0, 110.0, 90.0, 105.0, 1000.0))
            .build();
    // null candleData
    CandleRequest invalid3 =
        CandleRequest.builder().symbolData(new SymbolData("AAPL", "Apple Inc.")).build();

    List<CandleRequest> invalidRequests = java.util.Arrays.asList(invalid1, invalid2, invalid3);

    for (CandleRequest req : invalidRequests) {
      when(feedClientAdaptor.historicalDataAdaptor(any()))
          .thenReturn(Collections.singletonList(req));
      assertThrows(
          AppException.class, () -> feedDataHandler.handleHistoricalData(feedClientRequest));
    }
  }
}
