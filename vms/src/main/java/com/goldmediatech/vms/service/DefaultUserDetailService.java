package com.goldmediatech.vms.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.persistence.UserLoader;
import com.goldmediatech.vms.service.dto.UserDto;

@Service
public class DefaultUserDetailService implements UserDetailsService {

    private final UserLoader userLoader;

    public DefaultUserDetailService(UserLoader userLoader) {
        this.userLoader = userLoader;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userLoader.loadUser(UserDto.builder().username(username).build());
        return User
                .withUsername(user.username())
                .password(user.password())
                .authorities("ROLE_" + user.role()).build();
    }
}
