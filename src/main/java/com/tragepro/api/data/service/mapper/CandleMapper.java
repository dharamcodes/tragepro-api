package com.tragepro.api.data.service.mapper;

import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.exception.ServerException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.data.model.CandleData;
import com.tragepro.api.data.model.SymbolData;
import com.tragepro.api.data.model.entity.CandleEntity;
import com.tragepro.api.data.model.request.CandleRequest;
import com.tragepro.api.data.model.response.CandleResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface CandleMapper extends BaseMapper<CandleEntity, CandleRequest, CandleResponse> {

    @Override
    @InheritConfiguration(name = "toEntity")
    CandleEntity requestToEntity(CandleRequest candleRequest);

    @Override
    @org.mapstruct.Mapping(source = "id", target = "id")
    CandleResponse entityToResponse(CandleEntity candleEntity);

    @Override
    @InheritConfiguration(name = "toEntity")
    void merge(CandleRequest source, @MappingTarget CandleEntity target);

    default CandleData mapCandleData(CandleData source) {
        if (ObjectUtils.isEmpty(source)) {
            throw new ServerException(ErrorType.INTERNAL_ERROR);
        }
        return CandleData.builder()
                .timestamp(source.timestamp())
                .open(source.open())
                .high(source.high())
                .low(source.low())
                .close(source.close())
                .volume(source.volume())
                .build();
    }

    default SymbolData mapSymbolData(SymbolData source) {
        if (ObjectUtils.isEmpty(source)) {
            throw new ServerException(ErrorType.INTERNAL_ERROR);
        }
        return SymbolData.builder().id(source.id()).name(source.name()).build();
    }

    @Override
    default MapperType getType() {
        return MapperType.CANDLE_DATA_MAPPER;
    }
}
