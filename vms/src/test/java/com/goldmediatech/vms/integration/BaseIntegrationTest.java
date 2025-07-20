package com.goldmediatech.vms.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {

    @Value("${app.server.domain}")
    protected String domain;

    @LocalServerPort
    protected int port;

    protected final RestTemplate restTemplate = new RestTemplate();

    @BeforeEach
    protected void setUp() {
        // Set up RestTemplate to not throw exceptions for any status code
        // Otherwise restTemplate will throw an exception instead of returning a ResponseEntity with the error status.
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }
        });
    }

    protected String baseUrl(final String path) {
        return domain + ":" + port + path;
    }
}
