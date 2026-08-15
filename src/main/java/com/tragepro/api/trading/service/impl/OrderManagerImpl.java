package com.tragepro.api.trading.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.domain.trading.request.OrderRequest;
import com.tragepro.api.domain.trading.response.OrderResponse;
import com.tragepro.api.trading.service.OrderManager;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderManagerImpl implements OrderManager {

  private final Map<String, OrderResponse> orders = new ConcurrentHashMap<>();

  @Override
  public OrderResponse submitOrder(OrderRequest request) {
    if (request == null) {
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    log.info("Submitting order for symbol: {}", request.symbol());
    String id = UUID.randomUUID().toString();
    OrderResponse response =
        new OrderResponse(
            id,
            request.symbol(),
            request.quantity(),
            request.price(),
            request.orderType(),
            request.side(),
            "SUBMITTED",
            Instant.now());
    orders.put(id, response);
    return response;
  }

  @Override
  public OrderResponse cancelOrder(String orderId) {
    log.info("Cancelling order with id: {}", orderId);
    OrderResponse existing = getOrderStatus(orderId);
    OrderResponse cancelled =
        new OrderResponse(
            existing.id(),
            existing.symbol(),
            existing.quantity(),
            existing.price(),
            existing.orderType(),
            existing.side(),
            "CANCELLED",
            existing.createdAt());
    orders.put(orderId, cancelled);
    return cancelled;
  }

  @Override
  public OrderResponse getOrderStatus(String orderId) {
    log.info("Fetching order status for id: {}", orderId);
    if (orderId == null) {
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    OrderResponse order = orders.get(orderId);
    if (order == null) {
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return order;
  }
}
