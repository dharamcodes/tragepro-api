package com.tragepro.api.identity.service.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.domain.identity.entity.AuthenticationEntity;
import com.tragepro.api.domain.identity.request.AuthenticationRequest;
import com.tragepro.api.domain.identity.response.AuthenticationResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
public interface AuthenticationMapper
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
