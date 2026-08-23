package com.tragepro.api.datafeed.web;

import com.tragepro.api.common.model.response.PagedResponse;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "2. Market Data & Feeds", description = "Watchlist creation, management, and symbol tracking")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/watchlists")
public class WatchListController {

    private final WatchListService watchListService;

    @PostMapping
    public ResponseEntity<WatchListResponse> create(@Valid @RequestBody WatchListRequest request) {
        return ResponseEntity.ok().body(watchListService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WatchListResponse> getById(@PathVariable String id) {
        return watchListService
                .getById(id)
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<PagedResponse<WatchListResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok().body(PagedResponse.of(watchListService.getAll(pageable)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WatchListResponse> update(
            @NotNull @PathVariable String id, @Valid @RequestBody WatchListRequest request) {
        return ResponseEntity.ok().body(watchListService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable String id) {
        watchListService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WatchListResponse> patch(
            @NotNull @PathVariable String id, @Valid @RequestBody WatchListRequest request) {
        return ResponseEntity.ok().body(watchListService.patch(id, request));
    }
}
