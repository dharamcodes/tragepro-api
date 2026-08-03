package com.tragepro.api.datafeed.internal.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.datafeed.dto.SecurityRequest;
import com.tragepro.api.datafeed.dto.SecurityResponse;
import com.tragepro.api.datafeed.internal.entity.SecurityEntity;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapper.class)
public interface SecurityMapper
    extends BaseMapper<SecurityEntity, SecurityRequest, SecurityResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  SecurityEntity requestToEntity(SecurityRequest securityRequest);

  @Override
  SecurityResponse entityToResponse(SecurityEntity securityEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  void merge(SecurityRequest source, @org.mapstruct.MappingTarget SecurityEntity target);

  @Override
  default MapperType getType() {
    return MapperType.SECURITY_MAPPER;
  }
}
