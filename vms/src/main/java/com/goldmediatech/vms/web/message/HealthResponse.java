package com.goldmediatech.vms.web.message;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the health status response of the system.
 *
 * <p>This record is used to convey the current health status, services information,
 * and version of the application.</p>
 *
 * @param status   the overall health status of the system
 * @param services a description or list of services and their statuses
 * @param version  the current version of the application
 *
 */
@Schema(description = "Response body for the application's health status")
public record HealthResponse(
        @Schema(
                description = "The overall health status of the system",
                example = "UP",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String status,
        
        @Schema(
                description = "Service name.",
                example = "vms",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String services,
        
        @Schema(
                description = "The current version of the application",
                example = "1.0.0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String version
) {
}
