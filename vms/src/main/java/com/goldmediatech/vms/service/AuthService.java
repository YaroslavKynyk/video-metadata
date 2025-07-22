package com.goldmediatech.vms.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.goldmediatech.vms.configuration.JwtUtil;
import com.goldmediatech.vms.persistence.UserLoader;
import com.goldmediatech.vms.service.dto.JwtDto;
import com.goldmediatech.vms.service.dto.UserDto;

@Service
public class AuthService {

    private final UserLoader userLoader;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserLoader userLoader,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder) {
        this.userLoader = userLoader;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    public JwtDto authenticate(UserDto dto) {
        var user = userLoader.loadUser(dto);

        if (!passwordEncoder.matches(dto.password(), user.password())) {
            throw new BadCredentialsException("[AUTH] Invalid credentials");
        }

        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        return JwtDto.builder()
                .token(jwtUtil.generateToken(authentication))
                .build();
    }
}
