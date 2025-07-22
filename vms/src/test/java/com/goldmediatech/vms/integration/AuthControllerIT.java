package com.goldmediatech.vms.integration;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.goldmediatech.vms.web.message.AuthResponse;
import com.goldmediatech.vms.web.message.LoginRequest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;

@DisplayName("AuthController Integration Tests")
public class AuthControllerIT extends BaseIntegrationTest {

    @Test
    @DisplayName("[LOGIN] Status 200 and JWT returned when credentials are valid")
    void login_whenCredentialsAreValid_thenReturnJwt() {
        LoginRequest loginRequest = new LoginRequest("thor", "ThorPassword1@3");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(baseUrl("/auth/login"), request, AuthResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertNotNull(response.getBody().jwt()),
                () -> assertFalse(response.getBody().jwt().isEmpty()));
    }

    @Test
    @DisplayName("[LOGIN] Status 401 when username is incorrect")
    void login_whenUsernameIncorrect_thenFailWithUnauthorized() {
        LoginRequest loginRequest = new LoginRequest("idontexist", "SomePassword1@3");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl("/auth/login"), request, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @DisplayName("[LOGIN] Status 400 when password is incorrect")
    void login_whenPasswordIncorrect_thenFailWithBadRequest() {
        LoginRequest loginRequest = new LoginRequest("thor", "thorpassword123");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> request = new HttpEntity<>(loginRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl("/auth/login"), request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
