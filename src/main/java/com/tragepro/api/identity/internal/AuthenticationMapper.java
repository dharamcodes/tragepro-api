package com.tragepro.api.identity.internal;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.identity.model.entity.AuthenticationEntity;
import com.tragepro.api.identity.model.request.AuthenticationRequest;
import com.tragepro.api.identity.model.response.AuthenticationResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
interface AuthenticationMapper
    extends BaseMapper<AuthenticationEntity, AuthenticationRequest, AuthenticationResponse> {

  @Override
  @InheritConfiguration(name = "toEntity")
  @Mapping(target = "identifiers", ignore = true)
  AuthenticationEntity requestToEntity(AuthenticationRequest authenticationRequest);

  @Override
  AuthenticationResponse entityToResponse(AuthenticationEntity authenticationEntity);

  @Override
  @InheritConfiguration(name = "toEntity")
  @Mapping(target = "identifiers", ignore = true)
  void merge(AuthenticationRequest source, @MappingTarget AuthenticationEntity target);

  @Override
  default Class<?> getMapperClass() {
    return AuthenticationMapper.class;
  }
}
