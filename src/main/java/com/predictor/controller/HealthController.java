package com.predictor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "system", "Intelligent Student Performance & Career Path Predictor",
                "mlEngine", "ACTIVE (Ensemble Random Forest & Deep MLP)",
                "timestamp", LocalDateTime.now().toString(),
                "cloudReady", true
        ));
    }
}
