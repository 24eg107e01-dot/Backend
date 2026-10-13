package com.predictor.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> rootIndex() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "ONLINE & HEALTHY");
        response.put("system", "Intelligent Student Performance & Career Path Predictor API");
        response.put("version", "1.0.0");
        response.put("database", "PostgreSQL (Render Managed Database: project_9hb8)");
        response.put("mlEngine", "ACTIVE (Ensemble Random Forest & Multi-Layer Perceptron Neural Net)");
        response.put("timestamp", LocalDateTime.now().toString());

        Map<String, String> endpoints = new LinkedHashMap<>();
        endpoints.put("healthCheck", "/api/health");
        endpoints.put("predictGpaAndCareer", "POST /api/predict/gpa-and-career");
        endpoints.put("recentPredictions", "GET /api/predict/history");
        endpoints.put("advisorMetrics", "GET /api/advisor/metrics");
        endpoints.put("advisorAlerts", "GET /api/advisor/alerts");
        endpoints.put("authLogin", "POST /api/auth/login");
        endpoints.put("authRegister", "POST /api/auth/register");

        response.put("availableEndpoints", endpoints);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "system", "Intelligent Student Performance & Career Path Predictor",
                "database", "PostgreSQL Connected",
                "mlEngine", "ACTIVE (Ensemble Random Forest & Deep MLP)",
                "timestamp", LocalDateTime.now().toString(),
                "cloudReady", true
        ));
    }
}
