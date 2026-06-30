package com.tragepro.api.strategy.core.workflow.activity.impl;

import com.tragepro.api.common.context.WatchlistContext;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.common.util.CloneUtil;
import com.tragepro.api.strategy.constant.Exchange;
import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.constant.StrategyStep;
import com.tragepro.api.strategy.context.StrategyContextConfig;
import com.tragepro.api.strategy.core.workflow.activity.DataInitActivity;
import com.tragepro.api.strategy.model.*;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.props.StrategyConfig;
import com.tragepro.api.strategy.service.ConfigLoaderService;
import com.tragepro.api.strategy.service.StrategyService;
import com.tragepro.api.strategy.service.mapper.StrategyMapper;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitActivityImpl implements DataInitActivity {

  private final ConfigLoaderService configLoaderService;
  private final WatchlistContext watchlistContext;
  private final StrategyContextConfig strategyContextConfig;
  private final StrategyService strategyService;
  private final MapperFactory<StrategyMapper> mapperFactory;
  private final CloneUtil cloneUtil;

  @Override
  public Set<StrategyResponse> run(Set<StrategyRequest> strategyRequests) {
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    var entity = strategyRequests.stream().map(mapper::requestToEntity).collect(Collectors.toSet());
    var response = entity.stream().map(mapper::entityToResponse).collect(Collectors.toSet());
    response.forEach(resp -> updateContext(mapper, resp));
    return response;
  }

  @Override
  public StrategyRequest loadConfig(String strategyName) {
    var strategy = configLoaderService.getStrategyByName(strategyName);
    return StrategyRequest.builder()
        .strategy(getStrategy(strategy))
        .currentState(getCurrentState())
        .indicators(getIndicators(strategy))
        .timeframes(getTimeframe(strategy))
        .build();
  }

  @Override
  public Set<StrategyRequest> loadSymbol(StrategyRequest strategyRequest) {
    Set<StrategyRequest> response = new HashSet<>();
    watchlistContext
        .getWatchlist(strategyRequest.getStrategy().getWatchlist())
        .forEach(
            watchList -> {
              StrategyRequest strategyResponseClone =
                  cloneUtil.clone(strategyRequest, StrategyRequest.class);
              strategyResponseClone.setSymbolData(getSymbol(watchList));
              response.add(strategyResponseClone);
            });
    return response;
  }

  @Override
  public Set<StrategyRequest> storeData(Set<StrategyRequest> strategyRequests) {
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    return strategyRequests.stream()
        .map(strategyService::createOrUpdate)
        .map(mapper::responseToRequest)
        .collect(Collectors.toSet());
  }

  private void updateContext(StrategyMapper mapper, StrategyResponse response) {
    strategyContextConfig.put(mapper.toSymbolData(response.getSymbolData()), response);
  }

  private StrategyModel getStrategy(StrategyConfig strategyConfig) {
    return StrategyModel.builder()
        .name(strategyConfig.getName())
        .desc(strategyConfig.getDesc())
        .watchlist(strategyConfig.getWatchList())
        .build();
  }

  private SymbolModel getSymbol(SymbolData symbolData) {
    return SymbolModel.builder()
        .symbol(symbolData.symbol())
        .name(symbolData.name())
        .exchange(Exchange.NSE)
        .build();
  }

  private StatusModel getCurrentState() {
    return StatusModel.builder().state(StrategyState.INITIALIZING).step(StrategyStep.INIT).build();
  }

  private Set<IndicatorModel> getIndicators(StrategyConfig strategyConfig) {
    return strategyConfig.getIndicators().stream()
        .map(
            indicator ->
                IndicatorModel.builder()
                    .name(indicator.getName())
                    .timeFrame(indicator.getTimeFrame())
                    .build())
        .collect(Collectors.toSet());
  }

  private Set<TimeframeModel> getTimeframe(StrategyConfig strategyConfig) {
    return strategyConfig.getTimeframes().stream()
        .map(
            timeframe ->
                TimeframeModel.builder()
                    .value(timeframe.getValue())
                    .uom(timeframe.getUom())
                    .type(timeframe.getType())
                    .build())
        .collect(Collectors.toSet());
  }
}
