package com.tragepro.api.data.scheduler;

import com.tragepro.api.common.constant.ExchangeSegment;
import com.tragepro.api.common.constant.InstrumentType;
import com.tragepro.api.common.constant.IntervalType;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.WatchListResponse;
import com.tragepro.api.data.scheduler.adopter.FeedDataHandler;
import com.tragepro.api.data.service.SecurityService;
import com.tragepro.api.data.service.WatchListService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedDataScheduler {

  @Value("${data.watchlist.identifiers}")
  private String identifiers;

  @Value("${data.scheduler.fromDateDays}")
  private Integer interval;

  @Value("${data.scheduler.enableHistorical:false}")
  private boolean enableHistorical;

  @Value("${data.scheduler.enableIntraday:false}")
  private boolean enableIntraday;

  private final WatchListService watchlistService;
  private final FeedDataHandler feedDataHandler;
  private final SecurityService securityService;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  @Scheduled(cron = "*/10 * * * * *")
  public void scheduleHistorical() {
    if (!enableHistorical) return;
    WatchListResponse watchList =
        watchlistService
            .getById(identifiers)
            .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
    log.info("fetch watchlist historical :: {}", watchList.name());

    LocalDate toDate = LocalDate.now();
    LocalDate fromDate = toDate.minusDays(interval);

    List<FeedClientRequest> requestList =
        watchList.stocks().stream()
            .map(
                stock -> {
                  var securityEntry = securityService.fetSecurityBySymbol(stock.symbol());
                  return FeedClientRequest.builder()
                      .securityId(securityEntry.securityId())
                      .exchangeSegment(ExchangeSegment.of(securityEntry.exchange()).getValue())
                      .instrument(InstrumentType.of(securityEntry.instrument()).getValue())
                      .expiryCode(0)
                      .oi(true)
                      .fromDate(fromDate.format(formatter))
                      .toDate(toDate.format(formatter))
                      .build();
                })
            .toList();
    requestList.forEach(feedDataHandler::handleHistoricalData);
  }

  @Scheduled(cron = "*/10 * * * * *")
  public void scheduleIntraday() {
    if (!enableIntraday) return;
    WatchListResponse watchList =
        watchlistService
            .getById(identifiers)
            .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
    log.info("fetch watchlist intraday :: {}", watchList.name());

    LocalDate toDate = LocalDate.now();
    LocalDate fromDate = toDate.minusDays(interval);

    List<FeedClientRequest> requestList =
        watchList.stocks().stream()
            .map(
                stock -> {
                  var securityEntry = securityService.fetSecurityBySymbol(stock.symbol());
                  return FeedClientRequest.builder()
                      .securityId(securityEntry.securityId())
                      .exchangeSegment(ExchangeSegment.of(securityEntry.exchange()).getValue())
                      .instrument(InstrumentType.of(securityEntry.instrument()).getValue())
                      .interval(IntervalType.MIN_1.getValue())
                      .oi(true)
                      .fromDate(fromDate.format(formatter))
                      .toDate(toDate.format(formatter))
                      .build();
                })
            .toList();
    requestList.forEach(feedDataHandler::handleIntradayData);
  }
}
