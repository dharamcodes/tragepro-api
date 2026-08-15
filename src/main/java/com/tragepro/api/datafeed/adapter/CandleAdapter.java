package com.tragepro.api.datafeed.adapter;

import com.tragepro.api.domain.datafeed.request.CandleRequest;
import com.tragepro.api.domain.datafeed.response.CandleResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CandleAdapter {
  CandleResponse create(CandleRequest candleRequest);

  Optional<CandleResponse> getById(String id);

  Page<CandleResponse> getAll(Pageable pageable);

  Set<CandleResponse> getAll();

  CandleResponse update(String id, CandleRequest candleRequest);

  void delete(String id);

  boolean isCandleExists(String name, long timestamp);

  List<CandleResponse> getCandlesBySymbolAndDaysBack(String symbolName, int daysBack);

  Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols);
}
