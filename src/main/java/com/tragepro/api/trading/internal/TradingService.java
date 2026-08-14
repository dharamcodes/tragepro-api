package com.tragepro.api.trading.internal;

import com.tragepro.api.trading.model.TradePositionRequest;
import com.tragepro.api.trading.model.TradePositionResponse;
import java.util.List;

interface TradingService {

  TradePositionResponse openPosition(TradePositionRequest request);

  TradePositionResponse getPosition(String positionId);

  List<TradePositionResponse> getActivePositions();
}
