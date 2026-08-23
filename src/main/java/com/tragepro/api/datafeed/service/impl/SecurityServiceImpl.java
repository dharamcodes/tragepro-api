package com.tragepro.api.datafeed.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.datafeed.core.repository.SecurityRepository;
import com.tragepro.api.datafeed.service.SecurityService;
import com.tragepro.api.datafeed.service.mapper.SecurityMapper;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final SecurityRepository securityRepository;
    private final MapperFactory mapperFactory;

    @Override
    public SecurityResponse fetSecurityBySymbol(String symbol) {
        var mapper = mapperFactory.getMapper(SecurityMapper.class);
        if (!StringUtils.hasLength(symbol)) {
            log.error("Invalid symbol :: {}", symbol);
            throw new AppException(ErrorType.INVALID_PARAMETER);
        }
        var securityEntity = securityRepository.findBySymbol(symbol);
        if (Objects.isNull(securityEntity)) {
            log.error("Data not found for symbol :: {}", symbol);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        return mapper.entityToResponse(securityEntity);
    }
}
