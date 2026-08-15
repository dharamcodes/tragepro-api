package com.tragepro.api.datafeed.service.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.exception.ServerException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.domain.datafeed.SymbolDataModel;
import com.tragepro.api.domain.datafeed.entity.WatchListEntity;
import com.tragepro.api.domain.datafeed.request.WatchListRequest;
import com.tragepro.api.domain.datafeed.response.WatchListResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
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

  default void mapSymbolData(SymbolDataModel source) {
    if (ObjectUtils.isEmpty(source)) {
      throw new ServerException(ErrorType.INTERNAL_ERROR);
    }
    SymbolDataModel.builder().symbol(source.symbol()).name(source.name()).build();
  }

  @Override
  default Class<?> getMapperClass() {
    return WatchListMapper.class;
  }
}
