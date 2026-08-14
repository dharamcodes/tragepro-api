package com.tragepro.api.strategy.internal;

import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.util.ObjectCloneUtil;
import com.tragepro.api.common.workflow.BaseActivity;
import com.tragepro.api.datafeed.constant.Exchange;
import com.tragepro.api.datafeed.context.WatchlistContext;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import com.tragepro.api.datafeed.model.TimeframeModel;
import com.tragepro.api.strategy.constant.StrategyState;
import com.tragepro.api.strategy.constant.StrategyStep;
import com.tragepro.api.strategy.context.StrategyContext;
import com.tragepro.api.strategy.model.*;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import com.tragepro.api.strategy.props.StrategyConfig;
import com.tragepro.api.strategy.workflow.activity.DataInitActivity;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitActivityImpl extends BaseActivity implements DataInitActivity {

  private final ConfigLoaderService configLoaderService;
  private final WatchlistContext watchlistContext;
  private final StrategyContext strategyContext;
  private final StrategyService strategyService;
  private final MapperFactory mapperFactory;

  @Override
  public List<Class<?>> globalActivities() {
    return List.of(DataInitActivity.class);
  }

  @Override
  public List<Class<?>> localActivities() {
    return List.of(DataInitActivity.class);
  }

  @Override
  public Set<StrategyResponse> run(Set<StrategyRequest> strategyRequests) {
    var mapper = mapperFactory.getMapper(StrategyMapper.class);
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
                  ObjectCloneUtil.clone(strategyRequest, StrategyRequest.class);
              strategyResponseClone.setSymbolData(getSymbol(watchList));
              response.add(strategyResponseClone);
            });
    return response;
  }

  @Override
  public Set<StrategyRequest> storeData(Set<StrategyRequest> strategyRequests) {
    evaluateState(strategyRequests);
    var mapper = mapperFactory.getMapper(StrategyMapper.class);
    return strategyRequests.stream()
        .map(strategyService::createOrUpdate)
        .map(mapper::responseToRequest)
        .collect(Collectors.toSet());
  }

  private static void evaluateState(Set<StrategyRequest> strategyRequests) {
    strategyRequests.forEach(
        response -> response.setCurrentState(nextStatus(response.getCurrentState())));
  }

  private static StatusModel nextStatus(StatusModel current) {
    return StatusModel.builder().state(nextState(current)).step(nextStep(current)).build();
  }

  private static StrategyState nextState(StatusModel current) {
    if (Objects.isNull(current)) {
      return StrategyState.INITIALIZING;
    }
    return StrategyState.nextState(current.getState()).orElse(StrategyState.INITIALIZING);
  }

  private static StrategyStep nextStep(StatusModel current) {
    if (Objects.isNull(current)) {
      return StrategyStep.INIT;
    }
    return StrategyStep.nextStep(current.getStep()).orElse(StrategyStep.INIT);
  }

  private void updateContext(StrategyMapper mapper, StrategyResponse response) {
    strategyContext.put(response.getStrategy().getName(), response);
  }

  private StrategyModel getStrategy(StrategyConfig strategyConfig) {
    return StrategyModel.builder()
        .name(strategyConfig.getName())
        .desc(strategyConfig.getDesc())
        .watchlist(strategyConfig.getWatchList())
        .build();
  }

  private SymbolModel getSymbol(SymbolDataModel symbolDataModel) {
    return SymbolModel.builder()
        .symbol(symbolDataModel.symbol())
        .name(symbolDataModel.name())
        .exchange(Exchange.NSE)
        .build();
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
