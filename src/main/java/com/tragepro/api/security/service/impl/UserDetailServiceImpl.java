package io.tragepro.api.application.service.impl;

import io.tragepro.api.application.service.UserDetailService;
import io.tragepro.api.security.constant.RoleType;
import io.tragepro.api.security.repository.AuthenticationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {

    private final AuthenticationRepository authenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //        if (Strings.isEmpty(username)) {
        //            log.error("Invalid username :: {}", username);
        //            throw new AppException(ErrorType.INVALID_PARAMETER);
        //        }
        var userDetail = authenticationRepository.findByUserNameAndIsActive(username, true);
        //        if (Objects.isNull(userDetail)) {
        //            log.error("User not found, or have no access :: {}", username);
        //            throw new AppException(ErrorType.ACCESS_DENIED);
        //        }
        return User.builder()
                .username(userDetail.getUserName())
                .password(userDetail.getPassword())
                .roles(
                        Strings.isEmpty(userDetail.getRole().getValue())
                                ? RoleType.APP_USER.name()
                                : userDetail.getRole().getValue())
                .build();
    }
}
