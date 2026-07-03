package com.tragepro.api.strategy.service.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.strategy.model.SymbolModel;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
public interface StrategyMapper
    extends BaseMapper<StrategyEntity, StrategyRequest, StrategyResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  StrategyEntity requestToEntity(StrategyRequest strategyRequest);

  @Override
  StrategyResponse entityToResponse(StrategyEntity strategyEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  void merge(StrategyRequest source, @MappingTarget StrategyEntity target);

  default SymbolData toSymbolData(SymbolModel symbolModel) {
    return SymbolData.builder().symbol(symbolModel.getSymbol()).name(symbolModel.getName()).build();
  }

  @Override
  default MapperType getType() {
    return MapperType.STRATEGY_BUILDER_MAPPER;
  }

  StrategyRequest responseToRequest(StrategyResponse strategyResponse);
}
