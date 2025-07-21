package com.goldmediatech.vms.integration;

import static com.goldmediatech.vms.util.MessageQueue.TOPIC_VIDEO_METADATA;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
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
import com.goldmediatech.vms.util.MessageQueue;
import com.goldmediatech.vms.web.message.IngestRequest;

@DisplayName("Demo Async Video Metadata Import")
public class DemoImportVideoMetadataIT extends BaseIntegrationTest {

    @Autowired
    private AuthService authService;

    /**
     * Test to Demo asynchronous import of video metadata
     */
    @Test
    @Disabled("Demo purpose only. Run as standalone test to see async behavior")
    @DisplayName("Async call to import video metadata from external service")
    public void importVideoMetadata_asyncCall() {
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

        await();

        assertAll(
            () -> assertEquals(HttpStatus.OK, response.getStatusCode()),
            // first two messages are Platform thread on controller level
            () -> assertFalse(MessageQueue.receiveMessage(TOPIC_VIDEO_METADATA).contains("VirtualThread")),
            () -> assertFalse(MessageQueue.receiveMessage(TOPIC_VIDEO_METADATA).contains("VirtualThread")),
            // next three messages are VirtualThread on service level
            () -> assertTrue(MessageQueue.receiveMessage(TOPIC_VIDEO_METADATA).contains("VirtualThread")),
            () -> assertTrue(MessageQueue.receiveMessage(TOPIC_VIDEO_METADATA).contains("VirtualThread")),
            () -> assertTrue(MessageQueue.receiveMessage(TOPIC_VIDEO_METADATA).contains("VirtualThread"))
        );
    }

    /**
     * Never recommended to use such approach
     * Created for demo purpose only.
     */
    private void await() {
        int retry = 0;
        while (MessageQueue.count(TOPIC_VIDEO_METADATA) < 5 && retry < 10) { // wait for the async task to complete for max 10 seconds
            try {
                retry++;
                Thread.sleep(1000); // wait for a short period before checking again
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted while waiting for async task to complete", e);
            }
        }
    }
}
