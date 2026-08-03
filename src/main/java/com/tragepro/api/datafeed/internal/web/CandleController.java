package com.tragepro.api.datafeed.internal.web;

import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.CandleResponse;
import com.tragepro.api.datafeed.dto.PagedResponse;
import com.tragepro.api.datafeed.internal.service.CandleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "3. CandleController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/candles")
public class CandleController {

  private final CandleService candleService;

  @PostMapping
  public ResponseEntity<CandleResponse> create(@Valid @RequestBody CandleRequest candleRequest) {
    return ResponseEntity.ok().body(candleService.create(candleRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CandleResponse> getById(@PathVariable String id) {
    return candleService
        .getById(id)
        .map(response -> ResponseEntity.ok().body(response))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<PagedResponse<CandleResponse>> getAll(
      @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok().body(PagedResponse.of(candleService.getAll(pageable)));
  }

  @PutMapping("/{id}")
  public ResponseEntity<CandleResponse> update(
      @NotNull @PathVariable String id, @Valid @RequestBody CandleRequest candleRequest) {
    return ResponseEntity.ok().body(candleService.update(id, candleRequest));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@NotNull @PathVariable String id) {
    candleService.delete(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/latest")
  public ResponseEntity<Set<CandleResponse>> getLatestCandlesBySymbols(
      @RequestBody Set<String> symbols) {
    return ResponseEntity.ok().body(candleService.getLatestCandlesBySymbols(symbols));
  }
}
