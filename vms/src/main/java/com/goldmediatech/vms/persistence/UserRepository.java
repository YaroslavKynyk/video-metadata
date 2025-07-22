package com.goldmediatech.vms.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private static final Map<String, UserEntity> USERS_MOCK = new HashMap<>();

    public Optional<UserEntity> findByUsername(final String username) {
        // Simulate a user lookup
        return Optional.ofNullable(USERS_MOCK.get(username));
    }

    public void save(UserEntity user) {
        USERS_MOCK.put(user.getUsername(), user);
    }

    public static class UserEntity {
        private long id;
        private String username;
        private String password;
        private String role;

        public UserEntity(long id, String username, String password, String role) {
            this.id = id;
            this.username = username;
            this.password = password;
            this.role = role;
        }

        public long getId() {
            return id;
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
