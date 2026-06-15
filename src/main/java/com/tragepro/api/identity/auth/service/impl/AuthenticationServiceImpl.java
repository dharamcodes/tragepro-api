package com.tragepro.api.identity.auth.service.impl;

import com.tragepro.api.common.exception.AppException;
import com.tragepro.api.common.exception.constant.ErrorType;
import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.identity.auth.constant.RoleType;
import com.tragepro.api.identity.auth.helper.EmailHelper;
import com.tragepro.api.identity.auth.helper.JwtTokenHelper;
import com.tragepro.api.identity.auth.model.request.AuthenticationRequest;
import com.tragepro.api.identity.auth.model.request.LoginRequest;
import com.tragepro.api.identity.auth.model.request.ResetPasswordRequest;
import com.tragepro.api.identity.auth.model.response.AuthenticationResponse;
import com.tragepro.api.identity.auth.model.response.LoginResponse;
import com.tragepro.api.identity.auth.repository.AuthenticationRepository;
import com.tragepro.api.identity.auth.service.AuthenticationService;
import com.tragepro.api.identity.auth.service.mapper.AuthenticationMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private static final String ROLE = "role";
  private static final String PASSWORD_RESET_CLAIM = "resetPassword";

  private final AuthenticationManager authenticationManager;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final AuthenticationRepository authenticationRepository;
  private final MapperFactory<AuthenticationMapper> mapperFactory;
  private final JwtTokenHelper jwtTokenHelper;

  @Override
  public LoginResponse login(LoginRequest loginRequest) {
    var userDetails =
        authenticationRepository.findByUserNameAndIsActive(loginRequest.userName(), true);
    if (ObjectUtils.isEmpty(userDetails)) {
      log.error("User with userName {} does not exist", loginRequest.userName());
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    } else if (!bCryptPasswordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
      log.error("Invalid userName :: {}", loginRequest.userName());
      throw new AppException(ErrorType.ACCESS_DENIED);
    }
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.userName(), loginRequest.password()));
    var token =
        jwtTokenHelper.generateToken(
            loginRequest.userName(), Map.of(ROLE, userDetails.getRole().getValue()));
    return LoginResponse.builder().userName(loginRequest.userName()).token(token).build();
  }

  @Override
  public AuthenticationResponse signup(AuthenticationRequest authenticationRequest) {
    var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
    if (!ObjectUtils.isEmpty(
        authenticationRepository.findByUserName(authenticationRequest.userName()))) {
      log.error("User with given userName already exists {}", authenticationRequest.userName());
      throw new AppException(ErrorType.DATA_EXISTS);
    }
    var encodedPassword = bCryptPasswordEncoder.encode(authenticationRequest.password());
    var userEntity = mapper.requestToEntity(authenticationRequest);
    userEntity.setPassword(encodedPassword);

    String userName = authenticationRequest.userName();
    if (userName == null || userName.isBlank()) {
      userName = authenticationRequest.email();
    }
    userEntity.setUserName(userName);

    var userResponse = authenticationRepository.save(userEntity);
    return mapper.entityToResponse(userResponse);
  }

  @Override
  public AuthenticationResponse getByUserName(String userName) {
    var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
    var userDetails = authenticationRepository.findByUserName(userName);
    if (ObjectUtils.isEmpty(userDetails)) {
      log.error("User does not exists for userName :: {}", userName);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    return mapper.entityToResponse(userDetails);
  }

  @Override
  public AuthenticationResponse updateAuthenticationDetails(
      String userName, AuthenticationRequest authenticationRequest) {
    var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
    var authenticationDetails = authenticationRepository.findByUserName(userName);
    if (ObjectUtils.isEmpty(authenticationDetails)) {
      log.error("User with given userName not exists {}", authenticationRequest.userName());
      throw new AppException(ErrorType.USER_NOT_FOUND);
    }
    mapper.merge(authenticationRequest, authenticationDetails);
    log.info("Patched authEntity for userName:: {}", authenticationDetails.getUserName());
    var authDetailsEntity = authenticationRepository.save(authenticationDetails);
    return mapper.entityToResponse(authDetailsEntity);
  }

  @Override
  public void changePassword(ResetPasswordRequest resetPasswordRequest) {
    var userName = resetPasswordRequest.userName();
    if (!resetPasswordRequest.password().equals(resetPasswordRequest.confirmPassword())) {
      log.error("Password mismatch while resetting password for userName:: {} ", userName);
      throw new AppException(ErrorType.PASSWORD_MISMATCH);
    }
    var password = resetPasswordRequest.password();
    var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
    var authDetails = authenticationRepository.findByUserNameAndIsActive(userName, true);
    if (ObjectUtils.isEmpty(authDetails)) {
      log.error("Any active user with userName {} does not exists", userName);
      throw new AppException(ErrorType.USER_NOT_FOUND);
    }
    var mergeRequest =
        AuthenticationRequest.builder()
            .password(bCryptPasswordEncoder.encode(password))
            .isActive(true)
            .build();
    mapper.merge(mergeRequest, authDetails);
    authenticationRepository.save(authDetails);
  }

  @Override
  public void deactivateAuthentication(String userName) {
    var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
    var authenticationDetails = authenticationRepository.findByUserNameAndIsActive(userName, true);
    if (ObjectUtils.isEmpty(authenticationDetails)) {
      log.error("User with given userName does not exists {}", userName);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    var mergeRequest = AuthenticationRequest.builder().isActive(false).build();
    mapper.merge(mergeRequest, authenticationDetails);
    authenticationRepository.save(authenticationDetails);
  }

  @Override
  public void resetPassword(String userName) {
    var userDetails = authenticationRepository.findByUserNameAndIsActive(userName, true);
    if (ObjectUtils.isEmpty(userDetails)) {
      log.error("User with userName {} does not exists", userName);
      throw new AppException(ErrorType.USER_NOT_FOUND);
    }
    Map<String, String> claims =
        Map.of(PASSWORD_RESET_CLAIM, RoleType.PASSWORD_RESET_CLAIM.getValue());
    var token = jwtTokenHelper.generateResetPasswordToken(userName, claims);
    EmailHelper.sendPasswordResetEmail(userDetails.getEmail(), token);
  }

  @Override
  public void deleteAuthentication(String userName) {
    var authenticationDetails = authenticationRepository.findByUserName(userName);
    if (ObjectUtils.isEmpty(authenticationDetails)) {
      log.error("User with given userName does not exists {}", userName);
      throw new AppException(ErrorType.DATA_NOT_FOUND);
    }
    authenticationRepository.delete(authenticationDetails);
  }
}
