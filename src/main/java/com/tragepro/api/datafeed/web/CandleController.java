package com.tragepro.api.datafeed.web;

import com.tragepro.api.common.model.response.PagedResponse;
import com.tragepro.api.datafeed.adapter.CandleAdapter;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.response.CandleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
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

  private final CandleAdapter candleAdapter;

  @PostMapping
  public ResponseEntity<CandleResponse> create(@Valid @RequestBody CandleRequest candleRequest) {
    return ResponseEntity.ok().body(candleAdapter.create(candleRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CandleResponse> getById(@PathVariable String id) {
    return candleAdapter
        .getById(id)
        .map(response -> ResponseEntity.ok().body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<PagedResponse<CandleResponse>> getAll(
      @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok().body(PagedResponse.of(candleAdapter.getAll(pageable)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CandleResponse> update(
      @NotNull @PathVariable String id, @Valid @RequestBody CandleRequest candleRequest) {
    return ResponseEntity.ok().body(candleAdapter.update(id, candleRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@NotNull @PathVariable String id) {
    candleAdapter.delete(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/latest")
  public ResponseEntity<Set<CandleResponse>> getLatestCandlesBySymbols(
      @RequestBody Set<String> symbols) {
    return ResponseEntity.ok().body(candleAdapter.getLatestCandlesBySymbols(symbols));
  }
}
