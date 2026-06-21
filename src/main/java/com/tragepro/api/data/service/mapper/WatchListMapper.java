package com.tragepro.api.data.service.mapper;

import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.exception.ServerException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.common.model.SymbolData;
import com.tragepro.api.data.model.entity.WatchListEntity;
import com.tragepro.api.data.model.request.WatchListRequest;
import com.tragepro.api.data.model.response.WatchListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface WatchListMapper
    extends BaseMapper<WatchListEntity, WatchListRequest, WatchListResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  WatchListEntity requestToEntity(WatchListRequest watchListRequest);

  @Override
  @Mapping(source = "id", target = "id")
  WatchListResponse entityToResponse(WatchListEntity watchListEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  void merge(WatchListRequest source, @MappingTarget WatchListEntity target);

  default SymbolData mapSymbolData(SymbolData source) {
    if (ObjectUtils.isEmpty(source)) {
      throw new ServerException(ErrorType.INTERNAL_ERROR);
    }
    return SymbolData.builder().symbol(source.symbol()).name(source.name()).build();
  }

  @Override
  default MapperType getType() {
    return MapperType.WATCHLIST_MAPPER;
  }
}
