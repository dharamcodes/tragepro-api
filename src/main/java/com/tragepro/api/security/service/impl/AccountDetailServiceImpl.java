package com.tragepro.api.security.service.impl;

import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.security.model.request.AccountDetailRequest;
import com.tragepro.api.security.model.response.AccountDetailResponse;
import com.tragepro.api.security.repository.AccountDetailRepository;
import com.tragepro.api.security.repository.AuthenticationRepository;
import com.tragepro.api.security.service.AccountDetailService;
import com.tragepro.api.security.service.mapper.AccountDetailMapper;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@AllArgsConstructor
public class AccountDetailServiceImpl implements AccountDetailService {

    private final AccountDetailRepository accountDetailRepository;
    private final AuthenticationRepository authenticationRepository;
    private final MapperFactory<AccountDetailMapper> mapperFactory;

    @Override
    public AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest) {
        var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
        boolean accountExists =
                accountDetailRepository.findByEmailAndIsActive(accountDetailRequest.getEmail(), true) != null;
        if (accountExists) {
            log.error("Account with email '{}' already exists", accountDetailRequest.getEmail());
            throw new AppException(ErrorType.DATA_EXISTS);
        }
        var accountEntity = accountDetailRepository.save(mapper.requestToEntity(accountDetailRequest));
        log.info("Created account with identifier '{}'", accountEntity.getIdentifier());
        String username = getCurrentUsername();
        var authDetails = authenticationRepository.findByUserNameAndIsActive(username, true);
        var identifiers = Optional.ofNullable(authDetails.getIdentifiers()).orElseGet(HashSet::new);
        identifiers.add(accountEntity.getIdentifier());
        authDetails.setIdentifiers(identifiers);
        authenticationRepository.save(authDetails);
        return mapper.entityToResponse(accountEntity);
    }

    @Override
    public AccountDetailResponse getAccount(String identifier) {
        var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
        if (Strings.isBlank(identifier)) {
            log.error("Identifier can not be blank");
            throw new AppException(ErrorType.INVALID_PARAMETER);
        }
        var accountEntity = accountDetailRepository.findByIdentifier(identifier);
        if (Objects.isNull(accountEntity)) {
            log.error("Account not found for identifier {}", identifier);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        return mapper.entityToResponse(accountEntity);
    }

    @Override
    public AccountDetailResponse updateAccountDetails(String identifier, AccountDetailRequest accountDetailRequest) {
        var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
        var accountDetailsEntity = accountDetailRepository.findByIdentifier(identifier);
        if (ObjectUtils.isEmpty(accountDetailsEntity)) {
            log.error("Account with identifier '{}' does not exist", identifier);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        mapper.merge(accountDetailRequest, accountDetailsEntity);
        var accDetailsEntity = accountDetailRepository.save(accountDetailsEntity);
        return mapper.entityToResponse(accDetailsEntity);
    }

    @Override
    public void deactivateAccount(String identifier) {
        var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
        var accountDetailsEntity = accountDetailRepository.findByIdentifier(identifier);
        if (ObjectUtils.isEmpty(accountDetailsEntity)) {
            log.error("Account with given identifier does not exist {}", identifier);
            throw new AppException(ErrorType.DATA_NOT_FOUND);
        }
        var mergeRequest = AccountDetailRequest.builder().isActive(false).build();
        mapper.merge(mergeRequest, accountDetailsEntity);
        accountDetailRepository.save(accountDetailsEntity);
    }

    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
