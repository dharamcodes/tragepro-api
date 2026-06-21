package com.tragepro.api.data.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.request.CandleRequest;
import com.tragepro.api.common.model.response.CandleResponse;
import com.tragepro.api.data.repository.CandleRepository;
import com.tragepro.api.data.service.CandleService;
import com.tragepro.api.data.service.mapper.CandleMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CandleServiceImpl implements CandleService {

  private final CandleRepository candleRepository;
  private final MapperFactory<CandleMapper> mapperFactory;

  @Override
  public CandleResponse create(CandleRequest candleRequest) {
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    var candleEntity = mapper.requestToEntity(candleRequest);
    candleRepository.save(candleEntity);
    return mapper.entityToResponse(candleEntity);
  }

  @Override
  public Optional<CandleResponse> getById(String id) {
    var candleEntity = candleRepository.findById(id);
    if (candleEntity.isEmpty()) {
      log.error("candleEntity is empty or invalid for getById {}", candleEntity);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    return Optional.of(mapper.entityToResponse(candleEntity.get()));
  }

  @Override
  public Page<CandleResponse> getAll(Pageable pageable) {
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    var candleEntities = candleRepository.findAll(pageable);
    if (candleEntities.isEmpty()) {
      log.error("candleEntities is empty or invalid for getAll {}", candleEntities);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return candleEntities.map(mapper::entityToResponse);
  }

  @Override
  public CandleResponse update(String id, CandleRequest candleRequest) {
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    var candleEntity = candleRepository.findById(id);
    if (candleEntity.isEmpty()) {
      log.error("candleEntity is empty or invalid for update {}", candleEntity);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    mapper.merge(candleRequest, candleEntity.get());
    candleRepository.save(candleEntity.get());
    return mapper.entityToResponse(candleEntity.get());
  }

  @Override
  public void delete(String id) {
    var candleEntity = candleRepository.findById(id);
    var entityToDelete =
        candleEntity.orElseThrow(
            () -> {
              log.error("candleEntity is empty or invalid for delete, id: {}", id);
              return new AppException(ErrorType.DATA_NOT_FOUND);
            });
    candleRepository.delete(entityToDelete);
  }

  @Override
  public boolean isCandleExists(String name, long timestamp) {
    return candleRepository.existsBySymbolDataNameAndCandleDataTimestamp(name, timestamp);
  }

  @Override
  public java.util.List<CandleResponse> getCandlesBySymbolAndDaysBack(
      String symbolName, int daysBack) {
    long timestampFrom = Instant.now().minus(daysBack, ChronoUnit.DAYS).getEpochSecond();
    var candleEntities =
        candleRepository.findBySymbolDataNameAndCandleDataTimestampGreaterThanEqual(
            symbolName, timestampFrom);
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    return candleEntities.stream().map(mapper::entityToResponse).toList();
  }
}
