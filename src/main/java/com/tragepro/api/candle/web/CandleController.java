package com.tragepro.api.candle.web;

import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.model.response.CandleResponse;
import com.tragepro.api.candle.model.response.CandleSummaryResponse;
import com.tragepro.api.candle.service.CandleIngestionService;
import com.tragepro.api.candle.service.CandleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Candle",
        description = "Market candle CRUD, symbol queries, time-range queries, and manual ingestion trigger")
@RestController
@RequiredArgsConstructor
@RequestMapping("/config/v1/candle")
public class CandleController {

    private final CandleService candleService;
    private final CandleIngestionService candleIngestionService;

    @Operation(summary = "Create candle record", description = "Save a new candle record for a market symbol.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Record created successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error in request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @PostMapping
    public ResponseEntity<CandleResponse> create(@Valid @RequestBody CandleRequest candleRequest) {
        return ResponseEntity.ok().body(candleService.create(candleRequest));
    }

    @Operation(
            summary = "Get candle record by ID",
            description = "Fetch full detail for a single candle record by its document ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Record found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Record not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CandleResponse> getById(
            @Parameter(description = "MongoDB document ID of the candle record", required = true) @PathVariable
                    String id) {
        return ResponseEntity.ok(candleService.getById(id));
    }

    @Operation(
            summary = "Get all candle records",
            description =
                    "Paginated summary of all candle records. Default page=0, size=20. Returns empty page when collection is empty.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Records returned (may be an empty page)"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @GetMapping
    public ResponseEntity<Page<CandleSummaryResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok().body(candleService.getAll(pageable));
    }

    @Operation(
            summary = "Get candles by symbol",
            description =
                    "Paginated candle history for a single symbol. Index-backed — efficient on millions of documents.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Records returned (may be empty page if symbol has no data)"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @GetMapping("/symbol/{symbolId}")
    public ResponseEntity<Page<CandleSummaryResponse>> getBySymbol(
            @Parameter(description = "Symbol identifier (e.g. NIFTY50, RELIANCE)", required = true) @PathVariable
                    String symbolId,
            @PageableDefault(
                            size = 100,
                            sort = "candle.timestamp",
                            direction = org.springframework.data.domain.Sort.Direction.DESC)
                    Pageable pageable) {
        return ResponseEntity.ok().body(candleService.getBySymbol(symbolId, pageable));
    }

    @Operation(
            summary = "Get candles by symbol and time range",
            description =
                    "Time-range candle query for a single symbol. Ordered by timestamp ASC. Max 10,000 records returned.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Records returned (may be empty if no data in range)"),
        @ApiResponse(responseCode = "400", description = "Missing or invalid timestamp parameters"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @GetMapping("/symbol/{symbolId}/range")
    public ResponseEntity<List<CandleSummaryResponse>> getBySymbolAndTimeRange(
            @Parameter(description = "Symbol identifier", required = true) @PathVariable String symbolId,
            @Parameter(description = "Start of time range — epoch milliseconds inclusive", required = true)
                    @RequestParam
                    long from,
            @Parameter(description = "End of time range — epoch milliseconds inclusive", required = true) @RequestParam
                    long to) {
        return ResponseEntity.ok(candleService.getBySymbolAndTimeRange(symbolId, from, to));
    }

    @Operation(
            summary = "Get latest candle per symbol",
            description =
                    "Returns the most recent candle for every symbol. One record per symbol — full market snapshot.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "One record per symbol returned (may be empty if no data exists)"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @GetMapping("/symbols/latest")
    public ResponseEntity<List<CandleSummaryResponse>> getLatestPerSymbol() {
        return ResponseEntity.ok(candleService.getLatestPerSymbol());
    }

    @Operation(
            summary = "Manual bulk ingest",
            description =
                    "Trigger a bulk upsert of candle records without waiting for the scheduler. Returns count of affected documents.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bulk ingest completed — returns affected document count"),
        @ApiResponse(responseCode = "400", description = "Validation error in request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token")
    })
    @PostMapping("/ingest")
    public ResponseEntity<Integer> manualIngest(@Valid @RequestBody List<CandleRequest> records) {
        return ResponseEntity.ok(candleIngestionService.bulkUpsert(records));
    }

    @Operation(summary = "Update candle record", description = "Update an existing candle record by its document ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Record updated successfully"),
        @ApiResponse(responseCode = "400", description = "Validation error in request body"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Record not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CandleResponse> update(
            @Parameter(description = "MongoDB document ID of the record to update", required = true)
                    @NotNull
                    @PathVariable
                    String id,
            @Valid @RequestBody CandleRequest candleRequest) {
        return ResponseEntity.ok().body(candleService.update(id, candleRequest));
    }

    @Operation(summary = "Delete candle record", description = "Permanently delete a candle record by its document ID.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Record deleted successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized — missing or invalid token"),
        @ApiResponse(responseCode = "404", description = "Record not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "MongoDB document ID of the record to delete", required = true)
                    @NotNull
                    @PathVariable
                    String id) {
        candleService.delete(id);
        return ResponseEntity.ok().build();
    }
}
