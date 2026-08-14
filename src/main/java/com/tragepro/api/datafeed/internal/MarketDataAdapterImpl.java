package com.tragepro.api.datafeed.internal;

import com.tragepro.api.datafeed.MarketDataAdapter;
import com.tragepro.api.datafeed.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.request.WatchListRequest;
import com.tragepro.api.datafeed.model.response.CandleResponse;
import com.tragepro.api.datafeed.model.response.LoadCandleResponse;
import com.tragepro.api.datafeed.model.response.WatchListResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class MarketDataAdapterImpl implements MarketDataAdapter {

  private final CandleService candleService;
  private final DatafeedService datafeedService;
  private final WatchListService watchListService;

  @Override
  public List<CandleResponse> getLatestCandles(List<String> symbols) {
    Set<String> symbolSet = symbols != null ? new HashSet<>(symbols) : Set.of();
    Set<CandleResponse> candleResponses = candleService.getLatestCandlesBySymbols(symbolSet);
    return new ArrayList<>(candleResponses);
  }

  @Override
  public LoadCandleResponse loadData(LoadCandleRequest request) {
    return datafeedService.loadData(request);
  }

  @Override
  public CandleResponse createCandle(CandleRequest candleRequest) {
    return candleService.create(candleRequest);
  }

  @Override
  public Optional<CandleResponse> getCandleById(String id) {
    return candleService.getById(id);
  }

  @Override
  public Page<CandleResponse> getAllCandles(Pageable pageable) {
    return candleService.getAll(pageable);
  }

  @Override
  public CandleResponse updateCandle(String id, CandleRequest candleRequest) {
    return candleService.update(id, candleRequest);
  }

  @Override
  public void deleteCandle(String id) {
    candleService.delete(id);
  }

  @Override
  public Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols) {
    return candleService.getLatestCandlesBySymbols(symbols);
  }

  @Override
  public WatchListResponse createWatchList(WatchListRequest request) {
    return watchListService.create(request);
  }

  @Override
  public Optional<WatchListResponse> getWatchListById(String id) {
    return watchListService.getById(id);
  }

  @Override
  public Page<WatchListResponse> getAllWatchLists(Pageable pageable) {
    return watchListService.getAll(pageable);
  }

  @Override
  public Set<WatchListResponse> getAllWatchLists() {
    return watchListService.getAll();
  }

  @Override
  public WatchListResponse updateWatchList(String id, WatchListRequest request) {
    return watchListService.update(id, request);
  }

  @Override
  public void deleteWatchList(String id) {
    watchListService.delete(id);
  }

  @Override
  public WatchListResponse patchWatchList(String id, WatchListRequest request) {
    return watchListService.patch(id, request);
  }
}
