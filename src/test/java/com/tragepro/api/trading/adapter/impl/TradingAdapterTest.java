package com.tragepro.api.trading.adapter.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.tragepro.api.domain.trading.request.OrderRequest;
import com.tragepro.api.domain.trading.response.OrderResponse;
import com.tragepro.api.domain.trading.response.TradePositionResponse;
import com.tragepro.api.trading.service.OrderManager;
import com.tragepro.api.trading.service.TradingService;
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

  private OrderAdapterImpl orderAdapter;
  private TradingAdapterImpl tradingAdapter;

  @BeforeEach
  void setUp() {
    orderAdapter = new OrderAdapterImpl(orderManager);
    tradingAdapter = new TradingAdapterImpl(tradingService);
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

    OrderResponse response = orderAdapter.submitOrder(request);

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
