package com.predictor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponseDto {
    private Double predictedGpa;
    private Double predictedGpaMin;
    private Double predictedGpaMax;
    private String predictedGrade;
    private Double confidenceScore;

    // Academic Risk Engine Outputs
    private String riskLevel; // SAFE, MODERATE, CRITICAL
    private Double riskScore; // 0 - 100
    private List<String> riskFactors;
    private boolean earlyInterventionTriggered;
    private String earlyInterventionMessage;

    // Career Path Recommender Outputs
    private List<CareerSuggestionDto> topCareerRecommendations;

    // Explanatory Diagnostics
    private String modelUsed;
    private Map<String, Double> radarSkillScores;
    private List<String> personalizedAdvice;
    private String timestamp;
}
