package com.tragepro.api.trading;

import com.tragepro.api.trading.model.TradePositionRequest;
import com.tragepro.api.trading.model.TradePositionResponse;
import java.util.List;

public interface TradingService {

  TradePositionResponse openPosition(TradePositionRequest request);

  TradePositionResponse getPosition(String positionId);

  List<TradePositionResponse> getActivePositions();
}
