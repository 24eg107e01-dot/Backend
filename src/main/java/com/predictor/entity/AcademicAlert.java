package com.predictor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "academic_alerts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rollNumber;
    private String studentName;
    private String studentEmail;
    private String department;
    private Integer semester;

    @Enumerated(EnumType.STRING)
    private PredictionHistory.RiskLevel riskLevel; // SAFE, MODERATE, CRITICAL

    private Double predictedGpa;
    private Double attendancePercentage;

    @Column(columnDefinition = "TEXT")
    private String triggerReason; // e.g., "Predicted GPA < 6.0 and Attendance below 75%"

    @Column(columnDefinition = "TEXT")
    private String recommendedInterventionPlan;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AlertStatus status = AlertStatus.OPEN; // OPEN, IN_REVIEW, RESOLVED

    private String counselorNotes;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime resolvedAt;

    public enum AlertStatus {
        OPEN,
        IN_REVIEW,
        RESOLVED
    }
}
