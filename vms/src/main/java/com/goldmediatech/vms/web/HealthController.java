package com.goldmediatech.vms.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldmediatech.vms.util.GlobalExceptionHandler;
import com.goldmediatech.vms.web.message.HealthResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "Endpoint for checking the application's health status.")
@SecurityRequirement(name = "JWT Auth")
@SecurityScheme(
    name = "JWT Auth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer",
    description = "JWT Authentication for API access"
)
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = GlobalExceptionHandler.CODE_400_ERROR_MESSAGE),
        @ApiResponse(responseCode = "401", description = GlobalExceptionHandler.CODE_401_ERROR_MESSAGE),
        @ApiResponse(responseCode = "403", description = GlobalExceptionHandler.CODE_403_ERROR_MESSAGE),
        @ApiResponse(responseCode = "500", description = GlobalExceptionHandler.CODE_500_ERROR_MESSAGE)
        
})
public class HealthController {

    @Value("${app.version}")
    private String appVersion;

    @Value("${spring.application.name}")
    private String appName;
    

    @Operation(
        summary = "Get application health status",
        description = "Returns the current health status of the application, including its name and version. " +
                      "Accessible by users with 'ADMIN' or 'SYSTEM' roles.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Application is healthy.",
                content = @Content(schema = @Schema(implementation = HealthResponse.class))
            )
        }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
    public ResponseEntity<HealthResponse> health() {
        var response = new HealthResponse("UP", appName, appVersion);
        return ResponseEntity.ok(response);
    }
}
