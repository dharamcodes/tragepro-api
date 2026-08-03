package com.tragepro.api.identity.internal.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.identity.dto.AuthenticationRequest;
import com.tragepro.api.identity.dto.AuthenticationResponse;
import com.tragepro.api.identity.internal.entity.AuthenticationEntity;
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
  default MapperType getType() {
    return MapperType.AUTHENTICATION_MAPPER;
  }
}
