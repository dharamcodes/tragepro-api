package com.tragepro.api.identity.adapter.impl;

import com.tragepro.api.identity.adapter.UserDetailAdapter;
import com.tragepro.api.identity.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailAdapterImpl implements UserDetailAdapter {
    private final UserDetailService userDetailService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userDetailService.loadUserByUsername(username);
    }
}
