package com.tragepro.api.strategy.mapper;

import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.strategy.model.entity.StrategyEntity;
import com.tragepro.api.strategy.model.request.StrategyRequest;
import com.tragepro.api.strategy.model.response.StrategyResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface StrategyBuilderMapper
    extends BaseMapper<StrategyEntity, StrategyRequest, StrategyResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  StrategyEntity requestToEntity(StrategyRequest strategyRequest);

  @Override
  @Mapping(source = "id", target = "id")
  StrategyResponse entityToResponse(StrategyEntity strategyEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  void merge(StrategyRequest source, @MappingTarget StrategyEntity target);

  @Override
  default MapperType getType() {
    return MapperType.STRATEGY_BUILDER_MAPPER;
  }
}
