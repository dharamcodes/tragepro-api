package com.tragepro.api.strategy.builder;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.strategy.constant.BuilderSteps;
import com.tragepro.api.strategy.mapper.StrategyBuilderMapper;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StrategyBuilderImpl extends AbstractStrategyBuilder {

  private final MapperFactory<StrategyBuilderMapper> mapperFactory;

  private static final StrategyRequest.StrategyRequestBuilder request = StrategyRequest.builder();

  @Override
  public StrategyResponse build() {
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);
    var requestEntity = mapper.requestToEntity(request.build());
    return mapper.entityToResponse(requestEntity);
  }

  StrategyResponse strategyFactory(BuilderSteps builderSteps) {
    var mapper = mapperFactory.getMapper(MapperType.STRATEGY_BUILDER_MAPPER);

    if (builderSteps == null) {
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }

    switch (builderSteps) {
      case POPULATE_DATA -> populateData(request);
      case POPULATE_META -> populateMeta(request);
      case POPULATE_INDICATOR -> populateIndicator(request);
      default -> throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    var requestEntity = mapper.requestToEntity(request.build());
    return mapper.entityToResponse(requestEntity);
  }
}
