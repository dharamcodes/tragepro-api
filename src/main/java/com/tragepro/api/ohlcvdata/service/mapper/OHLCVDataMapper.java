package com.tragepro.api.ohlcvdata.service.mapper;

import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.exception.ServerException;
import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.ohlcvdata.model.OHLCVData;
import com.tragepro.api.ohlcvdata.model.SymbolData;
import com.tragepro.api.ohlcvdata.model.entity.OHLCVDataEntity;
import com.tragepro.api.ohlcvdata.model.request.OHLCVDataRequest;
import com.tragepro.api.ohlcvdata.model.response.OHLCVDataResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface OHLCVDataMapper extends BaseMapper<OHLCVDataEntity, OHLCVDataRequest, OHLCVDataResponse> {

    @Override
    @InheritConfiguration(name = "toEntity")
    OHLCVDataEntity requestToEntity(OHLCVDataRequest ohlcvDataRequest);

    @Override
    OHLCVDataResponse entityToResponse(OHLCVDataEntity ohlcvDataEntity);

    @Override
    @InheritConfiguration(name = "toEntity")
    void merge(OHLCVDataRequest source, @MappingTarget OHLCVDataEntity target);

    default OHLCVData mapOHLCVData(OHLCVData source) {
        if (ObjectUtils.isEmpty(source)) {
            throw new ServerException(ErrorType.INTERNAL_ERROR);
        }
        return OHLCVData.builder()
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
        return MapperType.OHLCV_DATA_MAPPER;
    }
}
