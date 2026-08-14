package com.tragepro.api.trading.internal;

import com.tragepro.api.trading.model.OrderRequest;
import com.tragepro.api.trading.model.OrderResponse;

interface OrderManager {

  OrderResponse submitOrder(OrderRequest request);

  OrderResponse cancelOrder(String orderId);

  OrderResponse getOrderStatus(String orderId);
}
