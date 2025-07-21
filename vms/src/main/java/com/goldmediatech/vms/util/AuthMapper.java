package com.goldmediatech.vms.util;

import com.goldmediatech.vms.persistence.UserRepository.UserEntity;
import com.goldmediatech.vms.service.dto.JwtDto;
import com.goldmediatech.vms.service.dto.UserDto;
import com.goldmediatech.vms.web.message.AuthResponse;
import com.goldmediatech.vms.web.message.LoginRequest;

public final class AuthMapper {

    private AuthMapper() {
        // Utility class, no instantiation
    }

    public static UserDto toUserDto(LoginRequest request) {
        return UserDto.builder()
                .username(request.username())
                .password(request.password())
                .build();
    }

    public static UserDto toUserDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }

    public static AuthResponse toAuthResponse(JwtDto dto) {
        return new AuthResponse(dto.token());
    }
}
