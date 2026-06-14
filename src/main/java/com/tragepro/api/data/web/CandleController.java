package com.tragepro.api.data.web;

import com.tragepro.api.data.model.request.CandleRequest;
import com.tragepro.api.data.model.response.CandleResponse;
import com.tragepro.api.data.model.response.PagedResponse;
import com.tragepro.api.data.service.CandleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
