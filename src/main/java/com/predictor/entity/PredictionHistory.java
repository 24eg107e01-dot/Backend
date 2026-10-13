package com.predictor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prediction_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PredictionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String rollNumber;
    private String studentName;

    // Predicted Output Metrics
    private Double predictedGpa;
    private Double predictedGpaMin;
    private Double predictedGpaMax;
    private Double confidenceScore;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel; // SAFE, MODERATE, CRITICAL

    private Double riskScore; // 0 - 100

    @Column(columnDefinition = "TEXT")
    private String riskFactorsJson; // Academic bottlenecks identified

    @Column(columnDefinition = "TEXT")
    private String recommendedCareersJson; // Array of ranked career suggestions

    @Column(columnDefinition = "TEXT")
    private String inputSnapshotJson; // Multi-dimensional input snapshot

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum RiskLevel {
        SAFE,
        MODERATE,
        CRITICAL
    }
}
