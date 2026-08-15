package com.tragepro.api.trading.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.domain.trading.request.TradePositionRequest;
import com.tragepro.api.domain.trading.response.TradePositionResponse;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TradingServiceImplTest {

  private TradingServiceImpl tradingService;

  @BeforeEach
  void setUp() {
    tradingService = new TradingServiceImpl();
  }

  @Test
  void testOpenPosition_Success() {
    TradePositionRequest request =
        new TradePositionRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "BUY");
    TradePositionResponse response = tradingService.openPosition(request);

    assertNotNull(response);
    assertNotNull(response.id());
    assertEquals("AAPL", response.symbol());
    assertEquals(BigDecimal.TEN, response.quantity());
    assertEquals(BigDecimal.valueOf(150.0), response.entryPrice());
    assertEquals("BUY", response.side());
    assertEquals("OPEN", response.status());
    assertNotNull(response.createdAt());
  }

  @Test
  void testOpenPosition_NullRequest() {
    AppException exception =
        assertThrows(AppException.class, () -> tradingService.openPosition(null));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testGetPosition_Success() {
    TradePositionRequest request =
        new TradePositionRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "BUY");
    TradePositionResponse created = tradingService.openPosition(request);

    TradePositionResponse fetched = tradingService.getPosition(created.id());
    assertNotNull(fetched);
    assertEquals(created.id(), fetched.id());
    assertEquals("AAPL", fetched.symbol());
  }

  @Test
  void testGetPosition_NullId() {
    AppException exception =
        assertThrows(AppException.class, () -> tradingService.getPosition(null));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testGetPosition_NotFound() {
    AppException exception =
        assertThrows(AppException.class, () -> tradingService.getPosition("non-existent"));
    assertEquals(ErrorType.DATA_NOT_FOUND, exception.getErrorType());
  }

  @Test
  void testGetActivePositions() {
    assertTrue(tradingService.getActivePositions().isEmpty());

    TradePositionRequest req1 =
        new TradePositionRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "BUY");
    TradePositionRequest req2 =
        new TradePositionRequest("GOOGL", BigDecimal.ONE, BigDecimal.valueOf(2800.0), "BUY");

    tradingService.openPosition(req1);
    tradingService.openPosition(req2);

    List<TradePositionResponse> active = tradingService.getActivePositions();
    assertEquals(2, active.size());
  }
}
