package com.tragepro.api.datafeed.service.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.domain.datafeed.entity.SecurityEntity;
import com.tragepro.api.domain.datafeed.request.SecurityRequest;
import com.tragepro.api.domain.datafeed.response.SecurityResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;

@Mapper(config = CommonMapper.class)
public interface SecurityMapper extends BaseMapper<SecurityEntity, SecurityRequest, SecurityResponse> {

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
