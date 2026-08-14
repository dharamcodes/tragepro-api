package com.tragepro.api.datafeed.internal;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.datafeed.model.entity.SecurityEntity;
import com.tragepro.api.datafeed.model.request.SecurityRequest;
import com.tragepro.api.datafeed.model.response.SecurityResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapper.class)
interface SecurityMapper extends BaseMapper<SecurityEntity, SecurityRequest, SecurityResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  SecurityEntity requestToEntity(SecurityRequest securityRequest);

  @Override
  SecurityResponse entityToResponse(SecurityEntity securityEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  void merge(SecurityRequest source, @org.mapstruct.MappingTarget SecurityEntity target);

  @Override
  default Class<?> getMapperClass() {
    return SecurityMapper.class;
  }
}
