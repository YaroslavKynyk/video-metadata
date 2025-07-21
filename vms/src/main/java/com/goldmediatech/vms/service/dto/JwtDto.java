package com.goldmediatech.vms.service.dto;

public record JwtDto(String token) {
    public static JwtDtoBuilder builder() {
        return new JwtDtoBuilder();
    }

    public static class JwtDtoBuilder {
        private String token;

        public JwtDtoBuilder token(String token) {
            this.token = token;
            return this;
        }

        public JwtDto build() {
            return new JwtDto(token);
        }
    }
}
