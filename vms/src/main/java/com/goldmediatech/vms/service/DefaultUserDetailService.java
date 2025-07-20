package com.goldmediatech.vms.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.persistence.UserRepository;

@Service
public class DefaultUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    public DefaultUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(entity -> User
                        .withUsername(entity.getUsername())
                        .password(entity.getPassword())
                        .authorities("ROLE_" + entity.getRole()).build())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("[AUTH] User %s not found", username)));
    }
}
