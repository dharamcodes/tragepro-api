package com.tragepro.api.trading.adapter.impl;

import com.tragepro.api.domain.trading.request.TradePositionRequest;
import com.tragepro.api.domain.trading.response.TradePositionResponse;
import com.tragepro.api.trading.adapter.TradingAdapter;
import com.tragepro.api.trading.service.TradingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TradingAdapterImpl implements TradingAdapter {
  private final TradingService tradingService;

  @Override
  public TradePositionResponse openPosition(TradePositionRequest request) {
    return tradingService.openPosition(request);
  }

  @Override
  public TradePositionResponse getPosition(String positionId) {
    return tradingService.getPosition(positionId);
  }

  @Override
  public List<TradePositionResponse> getActivePositions() {
    return tradingService.getActivePositions();
  }
}
