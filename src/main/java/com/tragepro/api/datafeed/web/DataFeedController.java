package com.tragepro.api.datafeed.web;

import com.tragepro.api.datafeed.service.DatafeedService;
import com.tragepro.api.domain.datafeed.request.LoadCandleRequest;
import com.tragepro.api.domain.datafeed.response.LoadCandleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "2. Market Data & Feeds", description = "Datafeed ingestion pipelines and symbol sync")
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
