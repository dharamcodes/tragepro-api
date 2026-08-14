package com.tragepro.api.trading.internal;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.trading.model.TradePositionRequest;
import com.tragepro.api.trading.model.TradePositionResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
class TradingServiceImpl implements TradingService {

  private final Map<String, TradePositionResponse> positions = new ConcurrentHashMap<>();

  @Override
  public TradePositionResponse openPosition(TradePositionRequest request) {
    if (request == null) {
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    log.info("Opening position for symbol: {}", request.symbol());
    String id = UUID.randomUUID().toString();
    TradePositionResponse response =
        new TradePositionResponse(
            id,
            request.symbol(),
            request.quantity(),
            request.entryPrice(),
            request.side(),
            "OPEN",
            Instant.now());
    positions.put(id, response);
    return response;
  }

  @Override
  public TradePositionResponse getPosition(String positionId) {
    log.info("Fetching position with id: {}", positionId);
    if (positionId == null) {
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    TradePositionResponse position = positions.get(positionId);
    if (position == null) {
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return position;
  }

  @Override
  public List<TradePositionResponse> getActivePositions() {
    log.info("Fetching all active positions");
    return new ArrayList<>(positions.values());
  }
}
