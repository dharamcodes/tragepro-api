package com.tragepro.api.datafeed.internal.service;

import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.datafeed.dto.WatchListResponse;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/** Domain service managing symbol watchlists. */
public interface WatchListService {

  WatchListResponse create(WatchListRequest request);

  Optional<WatchListResponse> getById(String id);

  Page<WatchListResponse> getAll(Pageable pageable);

  Set<WatchListResponse> getAll();

  WatchListResponse update(String id, WatchListRequest request);

  void delete(String id);

  WatchListResponse patch(String id, WatchListRequest request);
}
