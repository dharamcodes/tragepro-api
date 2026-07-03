package com.tragepro.api.data.scheduler.adopter;

import com.tragepro.api.common.constant.DataTimeType;
import com.tragepro.api.common.event.DataEvent;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.common.model.response.CandleResponse;
import com.tragepro.api.data.client.adopter.FeedClientAdaptor;
import com.tragepro.api.data.event.DataEventPublisher;
import com.tragepro.api.data.model.request.DataRequestWrapper;
import com.tragepro.api.data.service.CandleService;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDataHandler {

  private final FeedClientAdaptor feedClientAdaptor;
  private final CandleService candleService;
  private final DataEventPublisher dataEventPublisher;

  @Value("${data.fetch.daysBack}")
  private Integer daysBack;

  @Async("customSchedulerExecutor")
  public void handleHistoricalData(DataRequestWrapper feedClientRequestWrapper) {
    var feedClientRequest = feedClientRequestWrapper.getClientReq();
    List<CandleRequest> candleRequests = feedClientAdaptor.historicalDataAdaptor(feedClientRequest);
    if (candleRequests.isEmpty()) {
      log.error("No historical data found for feed request - {}", feedClientRequest.securityId());
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    CompletableFuture.completedFuture(
        candleRequests.stream()
            .map(candle -> candle.add(DataTimeType.HISTORICAL))
            .map(this::processCandle)
            .toList());
    var firstCandle = candleRequests.getFirst();
    var historicalData =
        candleService.getCandlesBySymbolAndDaysBack(firstCandle.symbolData().name(), daysBack);
    dataEventPublisher.publish(
        new DataEvent(feedClientRequest.securityId().toString(), historicalData));
  }

  @Async("customSchedulerExecutor")
  public void handleIntradayData(DataRequestWrapper feedClientRequestWrapper) {
    var feedClientRequest = feedClientRequestWrapper.getClientReq();
    List<CandleRequest> candleRequests = feedClientAdaptor.intradayDataAdaptor(feedClientRequest);
    if (candleRequests.isEmpty()) {
      log.error("No intraday found for feed request - {}", feedClientRequest.securityId());
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    candleRequests =
        candleRequests.stream()
            .map(v -> v.setSymbolData(feedClientRequestWrapper.getSymbolData()))
            .toList();
    CompletableFuture.completedFuture(
        candleRequests.stream()
            .map(candle -> candle.add(DataTimeType.INTRADAY))
            .map(this::processCandle)
            .toList());
    var firstCandle = candleRequests.getFirst();
    var historicalData =
        candleService.getCandlesBySymbolAndDaysBack(firstCandle.symbolData().name(), daysBack);
    dataEventPublisher.publish(
        new DataEvent(feedClientRequest.securityId().toString(), historicalData));
  }

  private CandleResponse processCandle(CandleRequest candle) {
    if (!isValid(candle)) {
      log.error("Invalid candle request - {}", candle);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }

    if (candleService.isCandleExists(candle.symbolData().name(), candle.candleData().timestamp())) {
      log.info(
          "Candle already exists, skipping save for {} at {}",
          candle.symbolData().name(),
          candle.candleData().timestamp());
      return CandleResponse.builder().build();
    }
    return Optional.ofNullable(candleService.create(candle))
        .orElseThrow(() -> new AppException(ErrorType.INTERNAL_ERROR));
  }

  private boolean isValid(CandleRequest candle) {
    return !ObjectUtils.isEmpty(candle)
        && !ObjectUtils.isEmpty(candle.symbolData())
        && StringUtils.hasText(candle.symbolData().name())
        && !ObjectUtils.isEmpty(candle.candleData());
  }
}
