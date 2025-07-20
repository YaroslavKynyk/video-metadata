package com.goldmediatech.vms.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldmediatech.vms.web.message.HealthResponse;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/health")
public class HealthController {
    
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SYSTEM')")
    public ResponseEntity<HealthResponse> health() {
        var response = new HealthResponse("UP", "vms", "1.0.0");
        return ResponseEntity.ok(response);
    }
}
