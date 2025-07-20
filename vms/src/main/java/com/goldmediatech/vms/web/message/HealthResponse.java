package com.goldmediatech.vms.web.message;

public record HealthResponse(
        String status,
        String services,
        String version
) {
}
