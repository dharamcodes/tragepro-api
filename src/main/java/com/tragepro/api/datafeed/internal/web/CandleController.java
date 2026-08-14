package com.tragepro.api.datafeed.internal.web;

import com.tragepro.api.common.model.response.PagedResponse;
import com.tragepro.api.datafeed.MarketDataAdapter;
import com.tragepro.api.datafeed.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.response.CandleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "3. CandleController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candles")
public class CandleController {

  private final MarketDataAdapter marketDataAdapter;

  @PostMapping
  public ResponseEntity<CandleResponse> create(@Valid @RequestBody CandleRequest candleRequest) {
    return ResponseEntity.ok().body(marketDataAdapter.createCandle(candleRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CandleResponse> getById(@PathVariable String id) {
    return marketDataAdapter
        .getCandleById(id)
        .map(response -> ResponseEntity.ok().body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<PagedResponse<CandleResponse>> getAll(
      @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok().body(PagedResponse.of(marketDataAdapter.getAllCandles(pageable)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CandleResponse> update(
      @NotNull @PathVariable String id, @Valid @RequestBody CandleRequest candleRequest) {
    return ResponseEntity.ok().body(marketDataAdapter.updateCandle(id, candleRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@NotNull @PathVariable String id) {
    marketDataAdapter.deleteCandle(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/latest")
  public ResponseEntity<java.util.Set<CandleResponse>> getLatestCandlesBySymbols(
      @RequestBody java.util.Set<String> symbols) {
    return ResponseEntity.ok().body(marketDataAdapter.getLatestCandlesBySymbols(symbols));
  }
}
