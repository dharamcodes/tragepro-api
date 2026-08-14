package com.tragepro.api.trading.internal;

import com.tragepro.api.trading.TradingAdapter;
import com.tragepro.api.trading.model.OrderRequest;
import com.tragepro.api.trading.model.OrderResponse;
import com.tragepro.api.trading.model.TradePositionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class TradingAdapterImpl implements TradingAdapter {

  private final OrderManager orderManager;
  private final TradingService tradingService;

  @Override
  public OrderResponse executeOrder(OrderRequest request) {
    return orderManager.submitOrder(request);
  }

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

  @Override
  public TradePositionResponse getPosition(String positionId) {
    return tradingService.getPosition(positionId);
  }
}
