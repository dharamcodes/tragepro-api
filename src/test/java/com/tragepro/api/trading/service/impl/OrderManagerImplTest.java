package com.tragepro.api.trading.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.domain.trading.request.OrderRequest;
import com.tragepro.api.domain.trading.response.OrderResponse;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderManagerImplTest {

  private OrderManagerImpl orderManager;

  @BeforeEach
  void setUp() {
    orderManager = new OrderManagerImpl();
  }

  @Test
  void testSubmitOrder_Success() {
    OrderRequest request =
        new OrderRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "LIMIT", "BUY");
    OrderResponse response = orderManager.submitOrder(request);

    assertNotNull(response);
    assertNotNull(response.id());
    assertEquals("AAPL", response.symbol());
    assertEquals(BigDecimal.TEN, response.quantity());
    assertEquals(BigDecimal.valueOf(150.0), response.price());
    assertEquals("LIMIT", response.orderType());
    assertEquals("BUY", response.side());
    assertEquals("SUBMITTED", response.status());
    assertNotNull(response.createdAt());
  }

  @Test
  void testSubmitOrder_NullRequest() {
    AppException exception = assertThrows(AppException.class, () -> orderManager.submitOrder(null));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testGetOrderStatus_Success() {
    OrderRequest request =
        new OrderRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "LIMIT", "BUY");
    OrderResponse submitted = orderManager.submitOrder(request);

    OrderResponse fetched = orderManager.getOrderStatus(submitted.id());
    assertNotNull(fetched);
    assertEquals(submitted.id(), fetched.id());
    assertEquals("SUBMITTED", fetched.status());
  }

  @Test
  void testGetOrderStatus_NullId() {
    AppException exception =
        assertThrows(AppException.class, () -> orderManager.getOrderStatus(null));
    assertEquals(ErrorType.INVALID_PARAMETER, exception.getErrorType());
  }

  @Test
  void testGetOrderStatus_NotFound() {
    AppException exception =
        assertThrows(AppException.class, () -> orderManager.getOrderStatus("non-existent"));
    assertEquals(ErrorType.DATA_NOT_FOUND, exception.getErrorType());
  }

  @Test
  void testCancelOrder_Success() {
    OrderRequest request =
        new OrderRequest("AAPL", BigDecimal.TEN, BigDecimal.valueOf(150.0), "LIMIT", "BUY");
    OrderResponse submitted = orderManager.submitOrder(request);

    OrderResponse cancelled = orderManager.cancelOrder(submitted.id());
    assertNotNull(cancelled);
    assertEquals(submitted.id(), cancelled.id());
    assertEquals("CANCELLED", cancelled.status());

    OrderResponse fetched = orderManager.getOrderStatus(submitted.id());
    assertEquals("CANCELLED", fetched.status());
  }

  @Test
  void testCancelOrder_NotFound() {
    AppException exception =
        assertThrows(AppException.class, () -> orderManager.cancelOrder("non-existent"));
    assertEquals(ErrorType.DATA_NOT_FOUND, exception.getErrorType());
  }
}
