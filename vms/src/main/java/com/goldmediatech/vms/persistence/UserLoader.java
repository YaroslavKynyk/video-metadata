package com.goldmediatech.vms.persistence;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.goldmediatech.vms.service.dto.UserDto;
import com.goldmediatech.vms.util.AuthMapper;

@Repository
public class UserLoader {
    
    private final UserRepository userRepository;

    public UserLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto loadUser(UserDto userDto) {
        return userRepository.findByUsername(userDto.getUsername())
                .map(entity -> AuthMapper.toUserDto(entity))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("[AUTH] User %s not found", userDto.getUsername())));
    }
}
