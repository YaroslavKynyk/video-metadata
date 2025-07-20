package com.goldmediatech.vms.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldmediatech.vms.service.AuthService;
import com.goldmediatech.vms.web.message.AuthResponse;
import com.goldmediatech.vms.web.message.LoginRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        var token = authService.authenticate(loginRequest);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
