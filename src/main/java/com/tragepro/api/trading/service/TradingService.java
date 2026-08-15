package com.tragepro.api.trading.service;

import com.tragepro.api.domain.trading.request.TradePositionRequest;
import com.tragepro.api.domain.trading.response.TradePositionResponse;
import java.util.List;

public interface TradingService {

  TradePositionResponse openPosition(TradePositionRequest request);

  TradePositionResponse getPosition(String positionId);

  List<TradePositionResponse> getActivePositions();
}
