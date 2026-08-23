package com.tragepro.api.identity.service.impl;

import com.tragepro.api.identity.core.repository.AuthenticationRepository;
import com.tragepro.api.identity.service.UserDetailService;
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
        if (userDetail == null) {
            throw new UsernameNotFoundException("User not found or inactive: " + username);
        }
        return User.builder()
                .username(userDetail.getUserName())
                .password(userDetail.getPassword())
                .roles(userDetail.getRole().getValue())
                .build();
    }
}
