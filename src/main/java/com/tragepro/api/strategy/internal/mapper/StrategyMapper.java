package com.tragepro.api.strategy.internal.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolDataModel;
import com.tragepro.api.strategy.dto.StrategyRequest;
import com.tragepro.api.strategy.dto.StrategyResponse;
import com.tragepro.api.strategy.dto.SymbolModel;
import com.tragepro.api.strategy.internal.entity.StrategyEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
public interface StrategyMapper
    extends BaseMapper<StrategyEntity, StrategyRequest, StrategyResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  StrategyEntity requestToEntity(StrategyRequest strategyRequest);

  @Override
  @Mapping(target = "id", source = "id")
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
  default MapperType getType() {
    return MapperType.STRATEGY_BUILDER_MAPPER;
  }

  StrategyRequest responseToRequest(StrategyResponse strategyResponse);
}
