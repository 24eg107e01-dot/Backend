package com.predictor.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String rollNumber;
    private String department;
    private Integer currentSemester;

    // Academic Continuous Assessment Metrics
    private Double attendancePercentage; // e.g., 88.5%
    private Double internalMarks;        // continuous assessment (0 - 100)
    private Double midtermScore;         // 0 - 100
    private Double labPracticalScore;    // 0 - 100
    private Double pastSemesterGpa;      // 0.00 - 10.00
    private Double weeklyStudyHours;     // hours spent per week

    // Foundational Skill Evaluation Metrics (0 - 100)
    private Double codingSkillScore;
    private Double analyticalMathScore;
    private Double problemSolvingScore;
    private Double communicationScore;
    private Double dataAiScore;
    private Double cloudSystemsScore;
    private Double coCurricularScore;

    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
