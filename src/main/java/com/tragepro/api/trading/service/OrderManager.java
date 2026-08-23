package com.tragepro.api.trading.service;

import com.tragepro.api.domain.trading.request.OrderRequest;
import com.tragepro.api.domain.trading.response.OrderResponse;

public interface OrderManager {

    OrderResponse submitOrder(OrderRequest request);

    OrderResponse cancelOrder(String orderId);

    OrderResponse getOrderStatus(String orderId);
}
