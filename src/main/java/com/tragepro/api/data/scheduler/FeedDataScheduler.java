package com.tragepro.api.data.scheduler;

import com.tragepro.api.common.constant.ExchangeSeg;
import com.tragepro.api.common.constant.InstrumentType;
import com.tragepro.api.common.constant.TimeInterval;
import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.data.model.request.DataRequestWrapper;
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

  @Scheduled(cron = "${data.scheduler.jobs.morning}")
  public void scheduleHistorical() {
    if (!enableHistorical) return;
    buildRequestList("fetch watchlist historical :: {}")
        .forEach(feedDataHandler::handleHistoricalData);
  }

  @Scheduled(cron = "${data.scheduler.jobs.evening}")
  public void scheduleIntraday() {
    if (!enableIntraday) return;
    buildRequestList("fetch watchlist intraday :: {}").forEach(feedDataHandler::handleIntradayData);
  }

  private List<DataRequestWrapper> buildRequestList(String logTemplate) {
    WatchListResponse watchList =
        watchlistService
            .getById(identifiers)
            .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
    log.info(logTemplate, watchList.name());

    LocalDate toDate = LocalDate.now();
    LocalDate fromDate = toDate.minusDays(interval);

    return watchList.stocks().stream()
        .map(
            stock -> {
              var securityEntry = securityService.fetSecurityBySymbol(stock.symbol());
              var request =
                  FeedClientRequest.builder()
                      .securityId(securityEntry.securityId())
                      .exchangeSegment(ExchangeSeg.of(securityEntry.exchange()).getValue())
                      .instrument(InstrumentType.of(securityEntry.instrument()).getValue())
                      .interval(TimeInterval.MIN_1.getValue())
                      .oi(true)
                      .fromDate(fromDate.format(formatter))
                      .toDate(toDate.format(formatter))
                      .build();
              var symbolData =
                  SymbolData.builder().symbol(stock.symbol()).name(stock.name()).build();
              return DataRequestWrapper.builder().clientReq(request).symbolData(symbolData).build();
            })
        .toList();
  }
}
