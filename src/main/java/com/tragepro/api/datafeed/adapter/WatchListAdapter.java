package com.tragepro.api.datafeed.adapter;

import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WatchListAdapter {
  WatchListResponse create(WatchListRequest request);

  Optional<WatchListResponse> getById(String id);

  Page<WatchListResponse> getAll(Pageable pageable);

  Set<WatchListResponse> getAll();

  WatchListResponse update(String id, WatchListRequest request);

  void delete(String id);

  WatchListResponse patch(String id, WatchListRequest request);
}
