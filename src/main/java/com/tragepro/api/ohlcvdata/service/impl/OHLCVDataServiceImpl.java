package com.tragepro.api.ohlcvdata.service.impl;

import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.ohlcvdata.model.request.OHLCVDataRequest;
import com.tragepro.api.ohlcvdata.model.response.OHLCVDataResponse;
import com.tragepro.api.ohlcvdata.repository.OHLCVDataRepository;
import com.tragepro.api.ohlcvdata.service.OHLCVDataService;
import com.tragepro.api.ohlcvdata.service.mapper.OHLCVDataMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OHLCVDataServiceImpl implements OHLCVDataService {

    private final OHLCVDataRepository ohlcvDataRepository;
    private final MapperFactory<OHLCVDataMapper> mapperFactory;

    @Override
    public OHLCVDataResponse create(OHLCVDataRequest ohlcvDataRequest) {
        var mapper = mapperFactory.getMapper(MapperType.OHLCV_DATA_MAPPER);
        var ohlcvDataEntity = mapper.requestToEntity(ohlcvDataRequest);
        ohlcvDataRepository.save(ohlcvDataEntity);
        return mapper.entityToResponse(ohlcvDataEntity);
    }

    @Override
    public Optional<OHLCVDataResponse> getById(String id) {
        var ohlcvDataEntity = ohlcvDataRepository.findById(id);
        if (ohlcvDataEntity.isEmpty()) {
            log.error("ohlcvDataEntity is empty or invalid for getById {}", ohlcvDataEntity);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        var mapper = mapperFactory.getMapper(MapperType.OHLCV_DATA_MAPPER);
        return Optional.of(mapper.entityToResponse(ohlcvDataEntity.get()));
    }

    @Override
    public Page<OHLCVDataResponse> getAll(Pageable pageable) {
        var mapper = mapperFactory.getMapper(MapperType.OHLCV_DATA_MAPPER);
        var ohlcvDataEntities = ohlcvDataRepository.findAll(pageable);
        if (ohlcvDataEntities.isEmpty()) {
            log.error("ohlcvDataEntities is empty or invalid for getAll {}", ohlcvDataEntities);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        return ohlcvDataEntities.map(mapper::entityToResponse);
    }

    @Override
    public OHLCVDataResponse update(String id, OHLCVDataRequest ohlcvDataRequest) {
        var mapper = mapperFactory.getMapper(MapperType.OHLCV_DATA_MAPPER);
        var ohlcvDataEntity = ohlcvDataRepository.findById(id);
        if (ohlcvDataEntity.isEmpty()) {
            log.error("ohlcvDataEntity is empty or invalid for update {}", ohlcvDataEntity);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        mapper.merge(ohlcvDataRequest, ohlcvDataEntity.get());
        ohlcvDataRepository.save(ohlcvDataEntity.get());
        return mapper.entityToResponse(ohlcvDataEntity.get());
    }

    @Override
    public void delete(String id) {
        var ohlcvDataEntity = ohlcvDataRepository.findById(id);
        var entityToDelete = ohlcvDataEntity.orElseThrow(() -> {
            log.error("ohlcvDataEntity is empty or invalid for delete, id: {}", id);
            return new AppException(ErrorType.DATA_NOT_FOUND);
        });
        ohlcvDataRepository.delete(entityToDelete);
    }
}
