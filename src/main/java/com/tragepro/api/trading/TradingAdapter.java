package com.tragepro.api.trading;

import com.tragepro.api.trading.model.OrderRequest;
import com.tragepro.api.trading.model.OrderResponse;
import com.tragepro.api.trading.model.TradePositionResponse;

public interface TradingAdapter {

  OrderResponse executeOrder(OrderRequest request);

  OrderResponse submitOrder(OrderRequest request);

  OrderResponse cancelOrder(String orderId);

  OrderResponse getOrderStatus(String orderId);

  TradePositionResponse getPosition(String positionId);
}
