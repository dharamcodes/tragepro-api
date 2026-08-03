package com.tragepro.api.datafeed.internal.web;

import com.tragepro.api.datafeed.dto.LoadCandleRequest;
import com.tragepro.api.datafeed.dto.LoadCandleResponse;
import com.tragepro.api.datafeed.internal.service.DatafeedService;
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

  private final DatafeedService datafeedService;

  @PostMapping("/load")
  public ResponseEntity<LoadCandleResponse> loadData(@RequestBody LoadCandleRequest request) {
    LoadCandleResponse response = datafeedService.loadData(request);
    return ResponseEntity.ok().body(response);
  }
}
