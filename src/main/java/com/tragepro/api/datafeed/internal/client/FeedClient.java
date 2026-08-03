package com.tragepro.api.datafeed.internal.client;

import com.tragepro.api.datafeed.dto.FeedClientRequest;
import com.tragepro.api.datafeed.dto.FeedClientResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange
public interface FeedClient {

  @PostExchange(url = "/charts/historical")
  FeedClientResponse getHistoricalFeed(@RequestBody FeedClientRequest feedClientRequest);

  @PostExchange(url = "/charts/intraday")
  FeedClientResponse getIntradayFeed(@RequestBody FeedClientRequest feedClientRequest);
}
