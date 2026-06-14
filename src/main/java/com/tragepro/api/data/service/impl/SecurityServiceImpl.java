package com.tragepro.api.data.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.data.model.response.SecurityResponse;
import com.tragepro.api.data.repository.SecurityRepository;
import com.tragepro.api.data.service.SecurityService;
import com.tragepro.api.data.service.mapper.SecurityMapper;
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
  private final MapperFactory<SecurityMapper> mapperFactory;

  @Override
  public SecurityResponse fetSecurityBySymbol(String symbol) {
    var mapper = mapperFactory.getMapper(MapperType.SECURITY_MAPPER);
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
