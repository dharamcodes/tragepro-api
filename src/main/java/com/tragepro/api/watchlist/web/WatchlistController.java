package com.tragepro.api.watchlist.web;

import com.tragepro.api.watchlist.model.request.WatchlistRequest;
import com.tragepro.api.watchlist.model.response.WatchlistMarketDataResponse;
import com.tragepro.api.watchlist.model.response.WatchlistResponse;
import com.tragepro.api.watchlist.service.WatchlistMarketDataService;
import com.tragepro.api.watchlist.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Watchlist", description = "Watchlist CRUD and Market Data APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;
    private final WatchlistMarketDataService watchlistMarketDataService;
    private final com.tragepro.api.watchlist.service.WatchlistLiveUpdateService watchlistLiveUpdateService;

    @Operation(summary = "Create a new watchlist")
    @PostMapping
    public ResponseEntity<WatchlistResponse> create(@Valid @RequestBody WatchlistRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(watchlistService.create(principal.getName(), request));
    }

    @Operation(summary = "Get all watchlists for the authenticated user")
    @GetMapping
    public ResponseEntity<List<WatchlistResponse>> getAll(Principal principal) {
        return ResponseEntity.ok(watchlistService.getAllForUser(principal.getName()));
    }

    @Operation(summary = "Get a specific watchlist by ID")
    @GetMapping("/{id}")
    public ResponseEntity<WatchlistResponse> getById(@PathVariable String id, Principal principal) {
        return ResponseEntity.ok(watchlistService.getById(id, principal.getName()));
    }

    @Operation(summary = "Update a watchlist")
    @PutMapping("/{id}")
    public ResponseEntity<WatchlistResponse> update(
            @PathVariable String id, @Valid @RequestBody WatchlistRequest request, Principal principal) {
        return ResponseEntity.ok(watchlistService.update(id, principal.getName(), request));
    }

    @Operation(summary = "Delete a watchlist")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id, Principal principal) {
        watchlistService.delete(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a symbol to a watchlist")
    @PostMapping("/{id}/symbols/{symbolId}")
    public ResponseEntity<WatchlistResponse> addSymbol(
            @PathVariable String id, @PathVariable String symbolId, Principal principal) {
        return ResponseEntity.ok(watchlistService.addSymbol(id, principal.getName(), symbolId));
    }

    @Operation(summary = "Remove a symbol from a watchlist")
    @DeleteMapping("/{id}/symbols/{symbolId}")
    public ResponseEntity<WatchlistResponse> removeSymbol(
            @PathVariable String id, @PathVariable String symbolId, Principal principal) {
        return ResponseEntity.ok(watchlistService.removeSymbol(id, principal.getName(), symbolId));
    }

    @Operation(summary = "Get sorted market data for all symbols in a watchlist")
    @GetMapping("/{id}/market-data")
    public ResponseEntity<List<WatchlistMarketDataResponse>> getMarketData(
            @PathVariable String id, Sort sort, Principal principal) {
        return ResponseEntity.ok(watchlistMarketDataService.getMarketDataForWatchlist(id, principal.getName(), sort));
    }

    @Operation(summary = "Stream live market data for a watchlist via SSE")
    @GetMapping(
            value = "/{id}/market-data/stream",
            produces = org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE)
    public org.springframework.web.servlet.mvc.method.annotation.SseEmitter streamMarketData(
            @PathVariable String id, Principal principal) {
        return watchlistLiveUpdateService.subscribe(id, principal.getName());
    }
}
