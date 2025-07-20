package com.goldmediatech.vms.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.configuration.JwtUtil;
import com.goldmediatech.vms.persistence.UserLoader;
import com.goldmediatech.vms.service.dto.JwtDto;
import com.goldmediatech.vms.service.dto.UserDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {

    private final UserLoader userLoader;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(UserLoader userLoader, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userLoader = userLoader;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public JwtDto authenticate(UserDto dto) {
        var user = userLoader.loadUser(dto);

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new BadCredentialsException("[AUTH] Invalid credentials");
        }

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        return JwtDto.builder()
                .token(jwtUtil.generateToken(authentication))
                .build();
    }
}
