package io.tragepro.api.application.service.mapper;

import io.tragepro.api.common.config.CommonMapperConfig;
import io.tragepro.api.common.mapper.BaseMapper;
import io.tragepro.api.common.mapper.MapperType;
import io.tragepro.api.security.model.entity.AccountDetailEntity;
import io.tragepro.api.security.model.request.AccountDetailRequest;
import io.tragepro.api.security.model.response.AccountDetailResponse;
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
