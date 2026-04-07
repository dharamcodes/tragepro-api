package com.tragepro.api.ohlcvdata.web;

import com.tragepro.api.ohlcvdata.model.request.OHLCVDataRequest;
import com.tragepro.api.ohlcvdata.model.response.OHLCVDataResponse;
import com.tragepro.api.ohlcvdata.service.OHLCVDataService;
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
@RequestMapping("/api/v1/ohlcvdata")
public class OHLCVDataController {

    private final OHLCVDataService ohlcvDataService;

    @PostMapping
    public ResponseEntity<OHLCVDataResponse> create(@Valid @RequestBody OHLCVDataRequest ohlcvDataRequest) {
        return ResponseEntity.ok().body(ohlcvDataService.create(ohlcvDataRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OHLCVDataResponse> getById(@PathVariable String id) {
        return ohlcvDataService
                .getById(id)
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<OHLCVDataResponse>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok().body(ohlcvDataService.getAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OHLCVDataResponse> update(
            @NotNull @PathVariable String id, @Valid @RequestBody OHLCVDataRequest ohlcvDataRequest) {
        return ResponseEntity.ok().body(ohlcvDataService.update(id, ohlcvDataRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@NotNull @PathVariable String id) {
        ohlcvDataService.delete(id);
        return ResponseEntity.ok().build();
    }
}
