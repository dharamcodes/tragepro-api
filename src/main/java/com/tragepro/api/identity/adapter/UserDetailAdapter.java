package com.tragepro.api.identity.adapter;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDetailAdapter {
  UserDetails loadUserByUsername(String username);
}
