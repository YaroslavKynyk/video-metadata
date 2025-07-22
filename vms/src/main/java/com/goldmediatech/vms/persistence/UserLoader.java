package com.goldmediatech.vms.persistence;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.goldmediatech.vms.service.dto.UserDto;

@Repository
public class UserLoader {
    
    private final UserRepository userRepository;

    public UserLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto loadUser(UserDto userDto) {
        return userRepository.findByUsername(userDto.username())
                .map(entity -> toUserDto(entity))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("[AUTH] User %s not found", userDto.username())));
    }

    public void registerUser(UserDto userDto) {
        var userEntity = new UserEntity(null, userDto.username(), userDto.password(), userDto.role());
        userRepository.save(userEntity);
    }

    private UserDto toUserDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .role(entity.getRole())
                .build();
    }
}
