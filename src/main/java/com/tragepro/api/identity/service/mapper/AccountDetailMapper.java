package com.tragepro.api.identity.service.mapper;

import com.tragepro.api.common.config.CommonMapper;
import com.tragepro.api.common.mapper.BaseMapper;
import com.tragepro.api.domain.identity.entity.AccountDetailEntity;
import com.tragepro.api.domain.identity.request.AccountDetailRequest;
import com.tragepro.api.domain.identity.response.AccountDetailResponse;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = CommonMapper.class)
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
    default Class<?> getMapperClass() {
        return AccountDetailMapper.class;
    }
}
