package com.tragepro.api.candle.service.mapper;

import com.tragepro.api.candle.model.entity.CandleEntity;
import com.tragepro.api.candle.model.request.CandleRequest;
import com.tragepro.api.candle.model.response.CandleResponse;
import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface CandleMapper extends BaseMapper<CandleEntity, CandleRequest, CandleResponse> {

    @Override
    @InheritConfiguration(name = "toEntity")
    CandleEntity requestToEntity(CandleRequest candleRequest);

    @Override
    CandleResponse entityToResponse(CandleEntity candleEntity);

    @Override
    @InheritConfiguration(name = "toEntity")
    void merge(CandleRequest source, @MappingTarget CandleEntity target);

    @Override
    default MapperType getType() {
        return MapperType.CANDLE_MAPPER;
    }
}
