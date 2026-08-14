package com.tragepro.api.datafeed;

import com.tragepro.api.datafeed.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.request.LoadCandleRequest;
import com.tragepro.api.datafeed.model.request.WatchListRequest;
import com.tragepro.api.datafeed.model.response.CandleResponse;
import com.tragepro.api.datafeed.model.response.LoadCandleResponse;
import com.tragepro.api.datafeed.model.response.WatchListResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarketDataAdapter {

  List<CandleResponse> getLatestCandles(List<String> symbols);

  LoadCandleResponse loadData(LoadCandleRequest request);

  CandleResponse createCandle(CandleRequest candleRequest);

  Optional<CandleResponse> getCandleById(String id);

  Page<CandleResponse> getAllCandles(Pageable pageable);

  CandleResponse updateCandle(String id, CandleRequest candleRequest);

  void deleteCandle(String id);

  Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols);

  WatchListResponse createWatchList(WatchListRequest request);

  Optional<WatchListResponse> getWatchListById(String id);

  Page<WatchListResponse> getAllWatchLists(Pageable pageable);

  Set<WatchListResponse> getAllWatchLists();

  WatchListResponse updateWatchList(String id, WatchListRequest request);

  void deleteWatchList(String id);

  WatchListResponse patchWatchList(String id, WatchListRequest request);
}
