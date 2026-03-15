package com.tragepro.api.security.service.mapper;

import com.tragepro.api.common.config.CommonMapperConfig;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.security.model.entity.AccountDetailEntity;
import com.tragepro.api.security.model.request.AccountDetailRequest;
import com.tragepro.api.security.model.response.AccountDetailResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapperConfig.class)
public interface AccountDetailMapper
        extends BaseMapper<AccountDetailEntity, AccountDetailRequest, AccountDetailResponse> {

    @Override
    @InheritConfiguration(name = "toEntity")
    AccountDetailEntity requestToEntity(AccountDetailRequest accountDetailRequest);

    @Override
    AccountDetailResponse entityToResponse(AccountDetailEntity accountDetailEntity);

    @Override
    @InheritConfiguration(name = "toEntity")
    void merge(AccountDetailRequest source, @MappingTarget AccountDetailEntity target);

    @Override
    default MapperType getType() {
        return MapperType.ACCOUNT_DETAIL_MAPPER;
    }
}
