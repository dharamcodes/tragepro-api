package com.tragepro.api.security.service.mapper;

import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.security.model.entity.AuthenticationEntity;
import com.tragepro.api.security.model.request.AuthenticationRequest;
import com.tragepro.api.security.model.response.AuthenticationResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
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
