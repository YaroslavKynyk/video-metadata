package com.goldmediatech.vms.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.goldmediatech.vms.service.AuthService;
import com.goldmediatech.vms.service.dto.JwtDto;
import com.goldmediatech.vms.service.dto.UserDto;
import com.goldmediatech.vms.web.message.IngestRequest;

@DisplayName("VideoMetadataController Integration Tests")
public class VideoMetadataControllerIT extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    @DisplayName("[VIDEO_IMPORT] Status 200 when ROLE_ADMIN")
    void importVideoMetadata_whenUserRoleAdmin_thenAllowExecution() {
        final JwtDto jwt = authService.authenticate(UserDto.builder()
                .username("odin")
                .password("thunder")
                .build());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwt.token());

        IngestRequest payload = new IngestRequest("source", "query", 10);
        HttpEntity<IngestRequest> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(
            baseUrl("/api/videos/import"),
             requestEntity,
              Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("[VIDEO_IMPORT] Status 403 when ROLE_USER")
    void importVideoMetadata_whenUserRoleNotAdmin_thenDenyExecution() {
        final JwtDto jwt = authService.authenticate(UserDto.builder()
                .username("thor")
                .password("hammer")
                .build());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(jwt.token());

        IngestRequest payload = new IngestRequest("source", "query", 10);
        HttpEntity<IngestRequest> requestEntity = new HttpEntity<>(payload, headers);
        ResponseEntity<Void> response = restTemplate.postForEntity(
            baseUrl("/api/videos/import"),
             requestEntity,
              Void.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
