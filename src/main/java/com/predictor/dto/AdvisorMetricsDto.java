package com.predictor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorMetricsDto {
    private long totalStudentsMonitored;
    private long criticalRiskCount;
    private long moderateRiskCount;
    private long safeCount;
    private long openAlertsCount;
    private long resolvedAlertsCount;
    private double averagePredictedGpa;
    private double retentionRiskRate; // percentage
}
