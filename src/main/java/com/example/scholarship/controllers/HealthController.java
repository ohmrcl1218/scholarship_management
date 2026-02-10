package com.example.scholarship.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {
    
    @GetMapping("/api/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "Scholarship Management API",
            "timestamp", LocalDateTime.now(),
            "environment", System.getenv("RAILWAY_ENVIRONMENT") != null ? "production" : "development"
        ));
    }
    
    @GetMapping("/")
    public ResponseEntity<?> root() {
        return ResponseEntity.ok(Map.of(
            "message", "Scholarship Management API",
            "version", "1.0.0",
            "status", "running",
            "docs", "/api/health for health check"
        ));
    }
}