package com.tragepro.api.strategy.internal.service;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.util.ObjectCloneUtil;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.WorkflowRequest;
import com.tragepro.api.strategy.dto.WorkflowResponse;
import com.tragepro.api.strategy.internal.constant.StrategyState;
import com.tragepro.api.strategy.internal.entity.StrategyEntity;
import com.tragepro.api.strategy.internal.mapper.StrategyMapper;
import com.tragepro.api.strategy.internal.repository.StrategyRepository;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyServiceImpl implements StrategyService {

  private final StrategyRepository strategyRepository;
  private final MapperFactory<StrategyMapper> mapperFactory;

  @Override
  public StrategyResponse create(StrategyRequest strategyRequest) {
    validateRequest(strategyRequest);
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    if (Objects.isNull(strategyRequest)) {
      log.error("StrategyRequest is null");
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    var entity = mapper.requestToEntity(strategyRequest);
    var savedEntity = strategyRepository.save(entity);
    return mapper.entityToResponse(savedEntity);
  }

  @Override
  public StrategyResponse getById(String id) {
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    var entity =
        strategyRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
    return mapper.entityToResponse(entity);
  }

  @Override
  public StrategyResponse createOrUpdate(StrategyRequest strategyRequest) {
    validateRequest(strategyRequest);

    var watchlist = strategyRequest.getStrategy().getWatchlist();
    var symbol = strategyRequest.getSymbolData().getSymbol();
    var state = strategyRequest.getCurrentState().getState();

    var existingEntityOpt =
        strategyRepository.findByStrategyWatchlistAndSymbolDataSymbolAndCurrentStateState(
            watchlist, symbol, state);

    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    return existingEntityOpt
        .map(existingEntity -> handleExistingStrategy(strategyRequest, existingEntity, mapper))
        .orElseGet(() -> createNewStrategy(strategyRequest, watchlist, symbol, state));
  }

  @Override
  public StrategyResponse update(String id, StrategyRequest strategyRequest) {
    validateRequest(strategyRequest);
    var existingEntity =
        strategyRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorType.DATA_NOT_FOUND));
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    return handleExistingStrategy(strategyRequest, existingEntity, mapper);
  }

  @Override
  public Set<StrategyResponse> getAll() {
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    List<StrategyEntity> strategyResponses = strategyRepository.findAll();
    if (strategyResponses.isEmpty()) {
      log.info("No strategies found in database");
      return java.util.Collections.emptySet();
    }
    return strategyResponses.stream().map(mapper::entityToResponse).collect(Collectors.toSet());
  }

  @Override
  public void delete(String id) {
    if (!strategyRepository.existsById(id)) {
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    strategyRepository.deleteById(id);
  }

  @Override
  public WorkflowResponse run(WorkflowRequest workflowRequest) {
    if (Objects.isNull(workflowRequest) || Objects.isNull(workflowRequest.getStrategyId())) {
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    log.info("Running strategy workflow for strategyId: {}", workflowRequest.getStrategyId());
    return WorkflowResponse.builder()
        .status("SUCCESS")
        .message("Strategy workflow executed for strategyId: " + workflowRequest.getStrategyId())
        .results(getAll())
        .build();
  }

  private void validateRequest(StrategyRequest strategyRequest) {
    if (Objects.isNull(strategyRequest)
        || Objects.isNull(strategyRequest.getStrategy())
        || Objects.isNull(strategyRequest.getSymbolData())
        || Objects.isNull(strategyRequest.getCurrentState())) {
      log.error("StrategyRequest or its required nested fields are null");
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }

    var watchlist = strategyRequest.getStrategy().getWatchlist();
    var symbol = strategyRequest.getSymbolData().getSymbol();
    var state = strategyRequest.getCurrentState().getState();

    if (Objects.isNull(watchlist) || Objects.isNull(symbol) || Objects.isNull(state)) {
      log.error(
          "StrategyRequest required values are null: watchlist={}, symbol={}, state={}",
          watchlist,
          symbol,
          state);
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
  }

  private StrategyResponse handleExistingStrategy(
      StrategyRequest strategyRequest, StrategyEntity existingEntity, StrategyMapper mapper) {
    var watchlist = strategyRequest.getStrategy().getWatchlist();
    var symbol = strategyRequest.getSymbolData().getSymbol();
    var state = strategyRequest.getCurrentState().getState();

    var mergedEntity = ObjectCloneUtil.clone(existingEntity, StrategyEntity.class);
    mapper.merge(strategyRequest, mergedEntity);

    if (mergedEntity.equals(existingEntity)) {
      log.info(
          "Strategy with watchlist {}, symbol {} and state {} is identical :: Skipping patch.",
          watchlist,
          symbol,
          state);
      return mapper.entityToResponse(existingEntity);
    }

    log.info(
        "Strategy with watchlist {}, symbol {} and state {} has changes :: Patching entry.",
        watchlist,
        symbol,
        state);
    var savedEntity = strategyRepository.save(mergedEntity);
    return mapper.entityToResponse(savedEntity);
  }

  private StrategyResponse createNewStrategy(
      StrategyRequest strategyRequest, String watchlist, String symbol, StrategyState state) {
    log.info(
        "Strategy with watchlist {}, symbol {} and state {} not found :: Creating new entry.",
        watchlist,
        symbol,
        state);
    return create(strategyRequest);
  }
}
