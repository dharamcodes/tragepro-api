package com.tragepro.api.trading.internal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.trading.model.OrderRequest;
import com.tragepro.api.trading.model.OrderResponse;
import com.tragepro.api.trading.model.TradePositionResponse;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TradingAdapterTest {

  @Mock private OrderManager orderManager;

  @Mock private TradingService tradingService;

  private TradingAdapterImpl tradingAdapter;

  @BeforeEach
  void setUp() {
    tradingAdapter = new TradingAdapterImpl(orderManager, tradingService);
  }

  @Test
  void testExecuteOrder() {
    OrderRequest request =
        new OrderRequest("AAPL", BigDecimal.valueOf(10), BigDecimal.valueOf(150), "LIMIT", "BUY");
    OrderResponse expectedResponse =
        new OrderResponse(
            "ord-1",
            "AAPL",
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(150),
            "LIMIT",
            "BUY",
            "SUBMITTED",
            Instant.now());
    when(orderManager.submitOrder(request)).thenReturn(expectedResponse);

    OrderResponse response = tradingAdapter.executeOrder(request);

    assertNotNull(response);
    assertEquals("ord-1", response.id());
    verify(orderManager).submitOrder(request);
  }

  @Test
  void testGetPosition() {
    TradePositionResponse expectedPosition =
        new TradePositionResponse(
            "pos-1",
            "AAPL",
            BigDecimal.valueOf(10),
            BigDecimal.valueOf(150),
            "BUY",
            "OPEN",
            Instant.now());
    when(tradingService.getPosition("pos-1")).thenReturn(expectedPosition);

    TradePositionResponse position = tradingAdapter.getPosition("pos-1");

    assertNotNull(position);
    assertEquals("pos-1", position.id());
    verify(tradingService).getPosition("pos-1");
  }
}
