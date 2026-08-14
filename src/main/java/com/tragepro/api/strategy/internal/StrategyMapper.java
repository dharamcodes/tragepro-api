package com.tragepro.api.strategy.internal;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import com.tragepro.api.strategy.model.SymbolModel;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
interface StrategyMapper extends BaseMapper<StrategyEntity, StrategyRequest, StrategyResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  StrategyEntity requestToEntity(StrategyRequest strategyRequest);

  @Override
  StrategyResponse entityToResponse(StrategyEntity strategyEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  void merge(StrategyRequest source, @MappingTarget StrategyEntity target);

  default SymbolDataModel toSymbolData(SymbolModel symbolModel) {
    return SymbolDataModel.builder()
        .symbol(symbolModel.getSymbol())
        .name(symbolModel.getName())
        .build();
  }

  @Override
  default Class<?> getMapperClass() {
    return StrategyMapper.class;
  }

  StrategyRequest responseToRequest(StrategyResponse strategyResponse);
}
