package com.tragepro.api.datafeed.adapter.impl;

import com.tragepro.api.datafeed.adapter.WatchListAdapter;
import com.tragepro.api.datafeed.service.WatchListService;
import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WatchListAdapterImpl implements WatchListAdapter {
  private final WatchListService watchListService;

  @Override
  public WatchListResponse create(WatchListRequest request) {
    return watchListService.create(request);
  }

  @Override
  public Optional<WatchListResponse> getById(String id) {
    return watchListService.getById(id);
  }

  @Override
  public Page<WatchListResponse> getAll(Pageable pageable) {
    return watchListService.getAll(pageable);
  }

  @Override
  public Set<WatchListResponse> getAll() {
    return watchListService.getAll();
  }

  @Override
  public WatchListResponse update(String id, WatchListRequest request) {
    return watchListService.update(id, request);
  }

  @Override
  public void delete(String id) {
    watchListService.delete(id);
  }

  @Override
  public WatchListResponse patch(String id, WatchListRequest request) {
    return watchListService.patch(id, request);
  }
}
