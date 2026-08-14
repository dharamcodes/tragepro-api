package com.tragepro.api.datafeed.internal.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.exception.ServerException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.datafeed.model.CandleDataModel;
import com.tragepro.api.datafeed.model.SymbolDataModel;
import com.tragepro.api.datafeed.model.entity.CandleEntity;
import com.tragepro.api.datafeed.model.request.CandleRequest;
import com.tragepro.api.datafeed.model.response.CandleResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface CandleMapper extends BaseMapper<CandleEntity, CandleRequest, CandleResponse> {

  @Override
  CandleEntity requestToEntity(CandleRequest candleRequest);

  @Override
  @Mapping(source = "id", target = "id")
  @Mapping(ignore = true, target = "dataTimeType")
  CandleResponse entityToResponse(CandleEntity candleEntity);

  @Override
  void merge(CandleRequest source, @MappingTarget CandleEntity target);

  default CandleDataModel mapCandleData(CandleDataModel source) {
    if (ObjectUtils.isEmpty(source)) {
      throw new ServerException(ErrorType.INTERNAL_ERROR);
    }
    return CandleDataModel.builder()
        .timestamp(source.timestamp())
        .open(source.open())
        .high(source.high())
        .low(source.low())
        .close(source.close())
        .volume(source.volume())
        .build();
  }

  default SymbolDataModel mapSymbolData(SymbolDataModel source) {
    if (ObjectUtils.isEmpty(source)) {
      throw new ServerException(ErrorType.INTERNAL_ERROR);
    }
    return SymbolDataModel.builder().symbol(source.symbol()).name(source.name()).build();
  }

  @Override
  default Class<?> getMapperClass() {
    return CandleMapper.class;
  }
}
