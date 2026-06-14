package com.tragepro.api.data.client;

import com.tragepro.api.data.model.request.FeedClientRequest;
import com.tragepro.api.data.model.response.FeedClientResponse;
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
