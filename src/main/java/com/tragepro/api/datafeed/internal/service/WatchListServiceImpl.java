package com.tragepro.api.datafeed.internal.service;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.datafeed.dto.WatchListRequest;
import com.tragepro.api.datafeed.dto.WatchListResponse;
import com.tragepro.api.datafeed.internal.mapper.WatchListMapper;
import com.tragepro.api.datafeed.internal.repository.WatchListRepository;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WatchListServiceImpl implements WatchListService {

  private final WatchListRepository watchListRepository;
  private final MapperFactory<WatchListMapper> mapperFactory;

  @Override
  public WatchListResponse create(WatchListRequest request) {
    var mapper = mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER);
    var watchListEntity = mapper.requestToEntity(request);
    var savedEntity = watchListRepository.save(watchListEntity);
    log.info("Created watchList with ID: {}", savedEntity.getId());
    return mapper.entityToResponse(savedEntity);
  }

  @Override
  public Optional<WatchListResponse> getById(String id) {
    var watchListEntity = watchListRepository.findById(id);
    if (watchListEntity.isEmpty()) {
      log.error("watchListEntity is empty or invalid for getById with ID: {}", id);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    var mapper = mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER);
    return Optional.of(mapper.entityToResponse(watchListEntity.get()));
  }

  @Override
  public Page<WatchListResponse> getAll(Pageable pageable) {
    var mapper = mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER);
    var watchListEntities = watchListRepository.getWatchListSummery(pageable);
    if (watchListEntities.isEmpty()) {
      log.error("No watchLists found or watchlist page is empty");
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return watchListEntities.map(mapper::entityToResponse);
  }

  @Override
  public Set<WatchListResponse> getAll() {
    var mapper = mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER);
    var watchListEntities = watchListRepository.findAll();
    if (watchListEntities.isEmpty()) {
      log.info("No watchLists found in database");
      return java.util.Collections.emptySet();
    }
    return watchListEntities.stream().map(mapper::entityToResponse).collect(Collectors.toSet());
  }

  @Override
  public WatchListResponse update(String id, WatchListRequest request) {
    var mapper = mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER);
    var watchListEntityOpt = watchListRepository.findById(id);
    if (watchListEntityOpt.isEmpty()) {
      log.error("watchListEntity is empty or invalid for update with ID: {}", id);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    var watchListEntity = watchListEntityOpt.get();
    mapper.merge(request, watchListEntity);
    var savedEntity = watchListRepository.save(watchListEntity);
    log.info("Updated watchList with ID: {}", savedEntity.getId());
    return mapper.entityToResponse(savedEntity);
  }

  @Override
  public void delete(String id) {
    var watchListEntity =
        watchListRepository
            .findById(id)
            .orElseThrow(
                () -> {
                  log.error("watchListEntity is empty or invalid for delete with ID: {}", id);
                  return new AppException(ErrorType.DATA_NOT_FOUND);
                });
    watchListRepository.delete(watchListEntity);
    log.info("Deleted watchList with ID: {}", id);
  }

  @Override
  public WatchListResponse patch(String id, WatchListRequest request) {
    var mapper = mapperFactory.getMapper(MapperType.WATCHLIST_MAPPER);
    var watchListEntity = watchListRepository.findById(id);
    watchListEntity.ifPresent(watchList -> mapper.merge(request, watchList));
    var savedEntity = watchListEntity.orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
    watchListRepository.save(savedEntity);
    log.info("Patched watchList with ID: {}", savedEntity.getId());
    return mapper.entityToResponse(savedEntity);
  }
}
