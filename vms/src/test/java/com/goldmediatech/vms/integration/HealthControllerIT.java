package com.goldmediatech.vms.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.goldmediatech.vms.service.AuthService;
import com.goldmediatech.vms.service.dto.JwtDto;
import com.goldmediatech.vms.service.dto.UserDto;
import com.goldmediatech.vms.web.message.HealthResponse;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("HealthController Integration Tests")
public class HealthControllerIT extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("[HEALTH] Status 200 when service is up and user role is ADMIN")
    void health_whenServiceIsUp_andUserRoleAdmin_thenReturnStatus200() {
        final JwtDto jwt = authService.authenticate(UserDto.builder()
                .username("odin")
                .password("OdinPassword1@3")
                .build());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwt.token());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<HealthResponse> response = restTemplate.exchange(
            baseUrl("/health"),
            HttpMethod.GET,
            entity,
            HealthResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("UP", response.getBody().status()),
                () -> assertTrue(response.getBody().services().contains("vms")),
                () -> assertNotNull(response.getBody().version()));
    }

    @Test
    @DisplayName("[HEALTH] Status 200 when service is up and user role is SYSTEM")
    void health_whenServiceIsUp_andUserRoleSystem_thenReturnStatus200() {
        final JwtDto jwt = authService.authenticate(UserDto.builder()
                .username("system")
                .password("SystemPassword1@3")
                .build());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwt.token());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<HealthResponse> response = restTemplate.exchange(
            baseUrl("/health"),
            HttpMethod.GET,
            entity,
            HealthResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
                () -> assertEquals("UP", response.getBody().status()),
                () -> assertTrue(response.getBody().services().contains("vms")),
                () -> assertNotNull(response.getBody().version()));
    }

    @Test
    @DisplayName("[HEALTH] Status 403 when user is not authenticated")
    void health_whenUserIsNotAuthenticated_thenReturnStatus403() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<HealthResponse> response = restTemplate.exchange(
            baseUrl("/health"),
            HttpMethod.GET,
            entity,
            HealthResponse.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("[HEALTH] Status 403 when user is not authorized")
    void health_whenUserIsNotAuthorized_thenReturnStatus403() {
        final JwtDto jwt = authService.authenticate(UserDto.builder()
                .username("thor")
                .password("ThorPassword1@3")
                .build());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwt.token());
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<HealthResponse> response = restTemplate.exchange(
            baseUrl("/health"),
            HttpMethod.GET,
            entity,
            HealthResponse.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
