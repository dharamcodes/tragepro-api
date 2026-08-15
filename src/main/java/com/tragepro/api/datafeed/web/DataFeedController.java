package com.tragepro.api.datafeed.web;

import com.tragepro.api.datafeed.adapter.DatafeedAdapter;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
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

  private final DatafeedAdapter datafeedAdapter;

  @PostMapping("/load")
  public ResponseEntity<LoadCandleResponse> loadData(@RequestBody LoadCandleRequest request) {
    LoadCandleResponse response = datafeedAdapter.loadData(request);
    return ResponseEntity.ok().body(response);
  }
}
