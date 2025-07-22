/**
 * Represents a request body for user login containing username and password.
 * <p>
 * This record is used to transfer login credentials from the client to the server.
 * Both fields are required and must not be blank.
 *
 * @param username the user's username; must not be blank
 * @param password the user's password; must not be blank
 */
package com.goldmediatech.vms.web.message;

import com.goldmediatech.vms.web.constraint.StrongPassword;
import com.goldmediatech.vms.web.constraint.StrongPasswordValidator;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


@Schema(description = "Request body for user login")
public record LoginRequest(
        @Schema(
            description = "User's username",
            example = "user",
            requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Username cannot be blank") String username,
        
        @Schema(
            description = "User's password. Must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character (@$!%*?&#^()_+=-).",
            example = "StrongP@ss1",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = StrongPasswordValidator.REGEXP_STRONG_PASSWORD
        )
        @NotBlank(message = "Password cannot be blank")
        @Pattern(
            regexp = StrongPasswordValidator.REGEXP_STRONG_PASSWORD,
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
        )
        @StrongPassword String password
) {}
