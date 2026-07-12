package com.tragepro.api.identity.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.identity.helper.JwtTokenHelper;
import com.tragepro.api.identity.model.request.AccountDetailRequest;
import com.tragepro.api.identity.model.response.AccountDetailResponse;
import com.tragepro.api.identity.repository.AccountDetailRepository;
import com.tragepro.api.identity.repository.AuthenticationRepository;
import com.tragepro.api.identity.service.AccountDetailService;
import com.tragepro.api.identity.service.mapper.AccountDetailMapper;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
@AllArgsConstructor
public class AccountDetailServiceImpl implements AccountDetailService {

  private static final String AUTHORIZATION = "Authorization";
  private final AccountDetailRepository accountDetailRepository;
  private final AuthenticationRepository authenticationRepository;
  private final MapperFactory<AccountDetailMapper> mapperFactory;
  private final JwtTokenHelper jwtTokenHelper;

  @Override
  public AccountDetailResponse createAccount(AccountDetailRequest accountDetailRequest) {
    var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
    boolean accountExists =
        accountDetailRepository.findByEmailAndIsActive(accountDetailRequest.email(), true) != null;
    if (accountExists) {
      log.error("Account with email '{}' already exists", accountDetailRequest.email());
      throw new AppException(ErrorType.DATA_EXISTS);
    }
    var accountEntity = accountDetailRepository.save(mapper.requestToEntity(accountDetailRequest));
    log.info("Created account with identifier '{}'", accountEntity.getIdentifier());
    String token = extractBearerTokenFromRequest();
    String username = jwtTokenHelper.extractUsername(token);
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
      log.error("Identifier can not be null {}", identifier);
      throw new AppException(ErrorType.INVALID_PARAMETER);
    }
    var accountEntity = accountDetailRepository.findByIdentifier(identifier);
    if (Objects.isNull(accountEntity)) {
      log.error("Account not foundfor identifier {}", identifier);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return mapper.entityToResponse(accountEntity);
  }

  @Override
  public AccountDetailResponse updateAccountDetails(
      String identifier, AccountDetailRequest accountDetailRequest) {
    var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
    var accountDetailsEntity = accountDetailRepository.findByIdentifier(identifier);
    if (ObjectUtils.isEmpty(accountDetailsEntity)) {
      log.error("User with given userName not exists {}", accountDetailRequest.identifier());
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    mapper.merge(accountDetailRequest, accountDetailsEntity);
    var accDetailsEntity = accountDetailRepository.save(accountDetailsEntity);
    return mapper.entityToResponse(accDetailsEntity);
  }

  @Override
  public void deactivateAccount(String identiFier) {
    var mapper = mapperFactory.getMapper(MapperType.ACCOUNT_DETAIL_MAPPER);
    var accountDetailsEntity = accountDetailRepository.findByIdentifier(identiFier);
    if (ObjectUtils.isEmpty(accountDetailsEntity)) {
      log.error("Account with given identifier does not exists {}", identiFier);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    var mergeRequest = AccountDetailRequest.builder().isActive(false).build();
    mapper.merge(mergeRequest, accountDetailsEntity);
    accountDetailRepository.save(accountDetailsEntity);
  }

  private String extractBearerTokenFromRequest() {
    var requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    var request = Objects.requireNonNull(requestAttributes).getRequest();
    String header = request.getHeader(AUTHORIZATION);
    return header.substring(7);
  }
}
