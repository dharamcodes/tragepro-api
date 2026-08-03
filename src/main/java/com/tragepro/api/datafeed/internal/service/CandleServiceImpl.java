package com.tragepro.api.datafeed.internal.service;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.datafeed.dto.CandleRequest;
import com.tragepro.api.datafeed.dto.CandleResponse;
import com.tragepro.api.datafeed.internal.entity.CandleEntity;
import com.tragepro.api.datafeed.internal.mapper.CandleMapper;
import com.tragepro.api.datafeed.internal.repository.CandleRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/** Service implementation managing market price candles, persistence, and queries. */
@Slf4j
@Service
@RequiredArgsConstructor
public class CandleServiceImpl implements CandleService {

  private final CandleRepository candleRepository;
  private final MapperFactory<CandleMapper> mapperFactory;

  /**
   * Creates and saves a new market price candle.
   *
   * @param candleRequest candle payload
   * @return created candle response
   */
  @Override
  public CandleResponse create(CandleRequest candleRequest) {
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    var candleEntity = mapper.requestToEntity(candleRequest);
    var savedEntity = candleRepository.save(candleEntity);
    return mapper.entityToResponse(savedEntity);
  }

  /**
   * Retrieves a candle by identifier.
   *
   * @param id candle identifier
   * @return optional containing the candle response if found
   */
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

  /**
   * Retrieves a paginated list of all candles.
   *
   * @param pageable pagination options
   * @return page of candle responses
   */
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

  /**
   * Retrieves a set of all candles.
   *
   * @return set of candle responses
   */
  @Override
  public Set<CandleResponse> getAll() {
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    List<CandleEntity> candleEntities = candleRepository.findAll();
    if (candleEntities.isEmpty()) {
      log.error("candleEntities is empty for getAll {}", candleEntities);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return candleEntities.stream().map(mapper::entityToResponse).collect(Collectors.toSet());
  }

  /**
   * Updates an existing candle by identifier.
   *
   * @param id candle identifier
   * @param candleRequest updated payload
   * @return updated candle response
   */
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

  /**
   * Deletes a candle by identifier.
   *
   * @param id candle identifier
   */
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

  /**
   * Checks if a candle exists for symbol name and timestamp.
   *
   * @param name symbol name
   * @param timestamp timestamp
   * @return true if exists, false otherwise
   */
  @Override
  public boolean isCandleExists(String name, long timestamp) {
    return candleRepository.existsBySymbolDataNameAndCandleDataTimestamp(name, timestamp);
  }

  /**
   * Retrieves candles for symbol and days back.
   *
   * @param symbolName symbol name
   * @param daysBack days back count
   * @return list of candle responses
   */
  @Override
  public List<CandleResponse> getCandlesBySymbolAndDaysBack(String symbolName, int daysBack) {
    long timestampFrom = Instant.now().minus(daysBack, ChronoUnit.DAYS).getEpochSecond();
    var candleEntities =
        candleRepository.findBySymbolDataNameAndCandleDataTimestampGreaterThanEqual(
            symbolName, timestampFrom);
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    return candleEntities.stream().map(mapper::entityToResponse).toList();
  }

  /**
   * Retrieves latest candles by symbols.
   *
   * @param symbols set of symbol names
   * @return set of candle responses
   */
  @Override
  public Set<CandleResponse> getLatestCandlesBySymbols(Set<String> symbols) {
    log.info("finding latest candles for symbols: {}", symbols);
    List<CandleEntity> latestCandles = candleRepository.findLatestCandlesBySymbols(symbols);
    if (latestCandles.isEmpty()) {
      log.warn("No candles found for the given symbols: {}", symbols);
      return new HashSet<>();
    }
    var mapper = mapperFactory.getMapper(MapperType.CANDLE_DATA_MAPPER);
    return latestCandles.stream().map(mapper::entityToResponse).collect(Collectors.toSet());
  }
}
