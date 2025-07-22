package com.goldmediatech.vms.service.dto;

public record UserDto(
    Long id,
    String username,
    String password,
    String role
) {
    public static UserDtoBuilder builder() {
        return new UserDtoBuilder();
    }

    public static class UserDtoBuilder {
        private Long id;
        private String username;
        private String password;
        private String role;

        public UserDtoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public UserDtoBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserDtoBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserDtoBuilder role(String role) {
            this.role = role;
            return this;
        }

        public UserDto build() {
            return new UserDto(id, username, password, role);
        }
    }
}

