package com.predictor.controller;

import com.predictor.dto.AdvisorMetricsDto;
import com.predictor.entity.AcademicAlert;
import com.predictor.service.AdvisorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/advisor")
@RequiredArgsConstructor
public class AdvisorController {

    private final AdvisorService advisorService;

    @GetMapping("/metrics")
    public ResponseEntity<AdvisorMetricsDto> getMetrics() {
        return ResponseEntity.ok(advisorService.getAdvisorMetrics());
    }

    @GetMapping("/alerts")
    public ResponseEntity<List<AcademicAlert>> getAlerts() {
        return ResponseEntity.ok(advisorService.getAllAlerts());
    }

    @PatchMapping("/alerts/{id}")
    public ResponseEntity<AcademicAlert> updateAlert(
            @PathVariable Long id,
            @RequestBody Map<String, String> payload) {
        
        String statusStr = payload.getOrDefault("status", "IN_REVIEW");
        String notes = payload.get("counselorNotes");
        AcademicAlert.AlertStatus status = AcademicAlert.AlertStatus.valueOf(statusStr.toUpperCase());

        return ResponseEntity.ok(advisorService.updateAlertStatus(id, status, notes));
    }
}
