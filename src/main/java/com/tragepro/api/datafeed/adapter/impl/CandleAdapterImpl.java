package com.tragepro.api.datafeed.adapter.impl;

import com.tragepro.api.datafeed.adapter.CandleAdapter;
import com.tragepro.api.datafeed.service.CandleService;
import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.response.CandleResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CandleAdapterImpl implements CandleAdapter {
    private final CandleService candleService;

    @Override
    public CandleResponse create(CandleRequest candleRequest) {
        return candleService.create(candleRequest);
    }

    @Override
    public Optional<CandleResponse> getById(String id) {
        return candleService.getById(id);
    }

    @Override
    public Page<CandleResponse> getAll(Pageable pageable) {
        return candleService.getAll(pageable);
    }

    @Override
    public Set<CandleResponse> getAll() {
        return candleService.getAll();
    }

    @Override
    public CandleResponse update(String id, CandleRequest candleRequest) {
        return candleService.update(id, candleRequest);
    }

    @Override
    public void delete(String id) {
        candleService.delete(id);
    }

    @Override
    public boolean isCandleExists(String name, long timestamp) {
        return candleService.isCandleExists(name, timestamp);
    }

    @Override
    public List<CandleResponse> getCandlesBySymbolAndDaysBack(String symbolName, int daysBack) {
        return candleService.getCandlesBySymbolAndDaysBack(symbolName, daysBack);
    }

    @Override
    public Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols) {
        return candleService.getLatestCandlesBySymbols(symbols);
    }
}
