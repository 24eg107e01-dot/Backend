package com.predictor.controller;

import com.predictor.dto.PredictionRequestDto;
import com.predictor.dto.PredictionResponseDto;
import com.predictor.entity.PredictionHistory;
import com.predictor.service.PredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predict")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping("/gpa-and-career")
    public ResponseEntity<PredictionResponseDto> predictPerformanceAndCareer(
            @RequestBody PredictionRequestDto request,
            Authentication auth) {
        
        String userEmail = (auth != null) ? auth.getName() : null;
        PredictionResponseDto response = predictionService.predictAndPersist(request, userEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<List<PredictionHistory>> getRecentPredictions() {
        return ResponseEntity.ok(predictionService.getRecentPredictions());
    }
}
