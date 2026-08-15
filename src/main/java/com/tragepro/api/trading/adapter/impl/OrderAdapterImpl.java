package com.tragepro.api.trading.adapter.impl;

import com.tragepro.api.domain.trading.request.OrderRequest;
import com.tragepro.api.domain.trading.response.OrderResponse;
import com.tragepro.api.trading.adapter.OrderAdapter;
import com.tragepro.api.trading.service.OrderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAdapterImpl implements OrderAdapter {
  private final OrderManager orderManager;

  @Override
  public OrderResponse submitOrder(OrderRequest request) {
    return orderManager.submitOrder(request);
  }

  @Override
  public OrderResponse cancelOrder(String orderId) {
    return orderManager.cancelOrder(orderId);
  }

  @Override
  public OrderResponse getOrderStatus(String orderId) {
    return orderManager.getOrderStatus(orderId);
  }
}
