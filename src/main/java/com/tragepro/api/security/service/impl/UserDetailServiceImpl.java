package com.tragepro.api.security.service.impl;

import com.tragepro.api.security.repository.AuthenticationRepository;
import com.tragepro.api.security.service.UserDetailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        var userDetail = authenticationRepository.findByUserNameAndIsActive(username, true);
        return User.builder()
                .username(userDetail.getUserName())
                .password(userDetail.getPassword())
                .roles(userDetail.getRole().getValue())
                .build();
    }
}
