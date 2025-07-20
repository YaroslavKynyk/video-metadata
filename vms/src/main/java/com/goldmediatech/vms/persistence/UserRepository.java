package com.goldmediatech.vms.persistence;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private static final Map<String, UserEntity> USERS_MOCK = Map.of(
        "system", new UserEntity("system", "roboto", "SYSTEM"),
        "odin", new UserEntity("odin", "thunder", "ADMIN"),
        "thor", new UserEntity("thor", "hammer", "USER")
    );

    public Optional<UserEntity> findByUsername(final String username) {
        // Simulate a user lookup
        return Optional.ofNullable(USERS_MOCK.get(username));
    }

    public static class UserEntity {
        private String username;
        private String password;
        private String role;

        public UserEntity(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getRole() {
            return role;
        }
    }
}
