package com.tragepro.api.strategy.internal.workflow.activity.impl;

import com.tragepro.api.common.constant.Exchange;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.common.model.TimeframeModel;
import com.tragepro.api.common.util.ObjectCloneUtil;
import com.tragepro.api.datafeed.DatafeedAdapter;
import com.tragepro.api.strategy.dto.IndicatorModel;
import com.tragepro.api.strategy.dto.StrategyModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.SymbolModel;
import com.tragepro.api.strategy.internal.context.StrategyContext;
import com.tragepro.api.strategy.internal.mapper.StrategyMapper;
import com.tragepro.api.strategy.internal.props.StrategyConfig;
import com.tragepro.api.strategy.internal.service.ConfigLoaderService;
import com.tragepro.api.strategy.internal.service.StrategyService;
import com.tragepro.api.strategy.internal.workflow.activity.BaseActivity;
import com.tragepro.api.strategy.internal.workflow.activity.DataInitActivity;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitActivityImpl implements DataInitActivity {

  private final ConfigLoaderService configLoaderService;
  private final DatafeedAdapter datafeedAdapter;
  private final StrategyContext strategyContext;
  private final StrategyService strategyService;
  private final MapperFactory<StrategyMapper> mapperFactory;

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
        .indicators(getIndicators(strategy))
        .timeframes(getTimeframe(strategy))
        .build();
  }

  @Override
  public Set<StrategyRequest> loadSymbol(StrategyRequest strategyRequest) {
    Set<StrategyRequest> response = new HashSet<>();
    datafeedAdapter
        .getWatchlistSymbols(strategyRequest.getStrategy().getWatchlist())
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
    BaseActivity.evaluateState(strategyRequests);
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    return strategyRequests.stream()
        .map(strategyService::createOrUpdate)
        .map(mapper::responseToRequest)
        .collect(Collectors.toSet());
  }

  private void updateContext(StrategyMapper mapper, StrategyResponse response) {
    if (response != null
        && response.getStrategy() != null
        && response.getStrategy().getName() != null) {
      strategyContext.put(response.getStrategy().getName(), response);
    }
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
    if (strategyConfig.getIndicators() == null) {
      return new HashSet<>();
    }
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
    if (strategyConfig.getTimeframes() == null) {
      return new HashSet<>();
    }
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
