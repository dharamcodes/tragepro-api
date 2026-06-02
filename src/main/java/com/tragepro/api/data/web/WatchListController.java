package com.tragepro.api.data.web;

import com.tragepro.api.data.model.request.WatchListRequest;
import com.tragepro.api.data.model.response.WatchListResponse;
import com.tragepro.api.data.service.WatchListService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Page<WatchListResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok().body(watchListService.getAll(pageable));
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
            @NotNull @PathVariable String id, @RequestBody WatchListRequest request) {
        return ResponseEntity.ok().body(watchListService.patch(id, request));
    }
}
