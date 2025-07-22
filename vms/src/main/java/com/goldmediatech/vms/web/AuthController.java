package com.goldmediatech.vms.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldmediatech.vms.service.AuthService;
import com.goldmediatech.vms.util.AuthMapper;
import com.goldmediatech.vms.util.GlobalExceptionHandler;
import com.goldmediatech.vms.web.message.AuthResponse;
import com.goldmediatech.vms.web.message.LoginRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "User authentication and JWT token generation endpoints.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = GlobalExceptionHandler.CODE_400_ERROR_MESSAGE),
        @ApiResponse(responseCode = "401", description = GlobalExceptionHandler.CODE_401_ERROR_MESSAGE),
        @ApiResponse(responseCode = "403", description = GlobalExceptionHandler.CODE_403_ERROR_MESSAGE),
        @ApiResponse(responseCode = "500", description = GlobalExceptionHandler.CODE_500_ERROR_MESSAGE)
        
})
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
        summary = "User Login",
        description = "Authenticates a user with provided credentials and returns a JWT token upon successful login. This token must be used for accessing protected endpoints.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(schema = @Schema(implementation = LoginRequest.class),
            examples = {
                @ExampleObject(name = "Admin User Login", summary = "Login as an administrator", value = """
                        {
                        "username": "odin",
                        "password": "OdinPassword1@3"
                        }
                        """),
                @ExampleObject(name = "Regular User Login", summary = "Login as a regular user", value = """
                        {
                        "username": "thor",
                        "password": "ThorPassword1@3"
                        }
                        """)
    })), responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful authentication. Returns a JWT token.",
                content = @Content(schema = @Schema(implementation = AuthResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var response = AuthMapper.toAuthResponse(
                authService.authenticate(
                        AuthMapper.toUserDto(loginRequest)));
        return ResponseEntity.ok(response);
    }
}
