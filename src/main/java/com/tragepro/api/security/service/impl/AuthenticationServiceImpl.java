package com.tragepro.api.security.service.impl;

import com.tragepro.api.common.mapper.MapperFactory;
import com.tragepro.api.common.mapper.MapperType;
import com.tragepro.api.exception.AppException;
import com.tragepro.api.exception.constant.ErrorType;
import com.tragepro.api.security.helper.EmailHelper;
import com.tragepro.api.security.helper.JwtTokenHelper;
import com.tragepro.api.security.model.request.AuthenticationRequest;
import com.tragepro.api.security.model.request.LoginRequest;
import com.tragepro.api.security.model.request.ResetPasswordRequest;
import com.tragepro.api.security.model.response.AuthenticationResponse;
import com.tragepro.api.security.model.response.LoginResponse;
import com.tragepro.api.security.repository.AuthenticationRepository;
import com.tragepro.api.security.service.AuthenticationService;
import com.tragepro.api.security.service.mapper.AuthenticationMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String ROLE = "role";
    private static final String PASSWORD_RESET_CLAIM = "resetPassword";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationRepository authenticationRepository;
    private final MapperFactory<AuthenticationMapper> mapperFactory;
    private final JwtTokenHelper jwtTokenHelper;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        var userDetails = authenticationRepository.findByUserNameAndIsActive(loginRequest.getUserName(), true);
        if (ObjectUtils.isEmpty(userDetails)) {
            log.error("User with userName {} does not exist", loginRequest.getUserName());
            throw new AppException(ErrorType.USER_NOT_FOUND);
        }
        var token = jwtTokenHelper.generateToken(
                loginRequest.getUserName(), Map.of(ROLE, userDetails.getRole().getValue()));
        return LoginResponse.builder()
                .userName(loginRequest.getUserName())
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse signup(AuthenticationRequest authenticationRequest) {
        var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
        if (!ObjectUtils.isEmpty(authenticationRepository.findByUserName(authenticationRequest.getUserName()))) {
            log.error("User with given userName already exists {}", authenticationRequest.getUserName());
            throw new AppException(ErrorType.DATA_EXISTS);
        }
        if (authenticationRequest.getUserName().isBlank()) {
            authenticationRequest.setUserName(authenticationRequest.getEmail());
        }
        var encodedPassword = passwordEncoder.encode(authenticationRequest.getPassword());
        var userEntity = mapper.requestToEntity(authenticationRequest);
        userEntity.setPassword(encodedPassword);
        var userResponse = authenticationRepository.save(userEntity);
        return mapper.entityToResponse(userResponse);
    }

    @Override
    public AuthenticationResponse getByUserName(String userName) {
        var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
        var userDetails = authenticationRepository.findByUserName(userName);
        if (ObjectUtils.isEmpty(userDetails)) {
            log.error("User does not exist for userName :: {}", userName);
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
            log.error("User with given userName does not exist {}", userName);
            throw new AppException(ErrorType.USER_NOT_FOUND);
        }
        mapper.merge(authenticationRequest, authenticationDetails);
        log.info("Patched authEntity for userName:: {}", authenticationDetails.getUserName());
        var authDetailsEntity = authenticationRepository.save(authenticationDetails);
        return mapper.entityToResponse(authDetailsEntity);
    }

    @Override
    public void changePassword(ResetPasswordRequest resetPasswordRequest) {
        var userName = resetPasswordRequest.getUserName();
        if (!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())) {
            log.error("Password mismatch while resetting password for userName:: {} ", userName);
            throw new AppException(ErrorType.PASSWORD_MISMATCH);
        }
        var mapper = mapperFactory.getMapper(MapperType.AUTHENTICATION_MAPPER);
        var authDetails = authenticationRepository.findByUserNameAndIsActive(userName, true);
        if (ObjectUtils.isEmpty(authDetails)) {
            log.error("No active user with userName {} exists", userName);
            throw new AppException(ErrorType.USER_NOT_FOUND);
        }
        var mergeRequest = AuthenticationRequest.builder()
                .password(passwordEncoder.encode(resetPasswordRequest.getPassword()))
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
            log.error("User with given userName does not exist {}", userName);
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
            log.error("User with userName {} does not exist", userName);
            throw new AppException(ErrorType.USER_NOT_FOUND);
        }
        Map<String, String> claims = Map.of(PASSWORD_RESET_CLAIM, "PASSWORD_RESET_CLAIM");
        var token = jwtTokenHelper.generateResetPasswordToken(userName, claims);
        EmailHelper.sendPasswordResetEmail(userDetails.getEmail(), token);
    }
}
