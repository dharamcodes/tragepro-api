package com.tragepro.api.datafeed.internal.web;

import com.tragepro.api.datafeed.MarketDataAdapter;
import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.response.LoadCandleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "4. DataFeedController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/datafeed")
public class DataFeedController {

  private final MarketDataAdapter marketDataAdapter;

  @PostMapping("/load")
  public ResponseEntity<LoadCandleResponse> loadData(@RequestBody LoadCandleRequest request) {
    LoadCandleResponse response = marketDataAdapter.loadData(request);
    return ResponseEntity.ok().body(response);
  }
}
