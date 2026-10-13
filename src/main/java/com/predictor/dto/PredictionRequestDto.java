package com.predictor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PredictionRequestDto {
    // Student Identity (Optional if guest or testing)
    private String studentName;
    private String rollNumber;
    private String department;
    private Integer semester;

    // Academic Metrics
    private Double attendancePercentage; // 0 - 100 %
    private Double internalMarks;        // Continuous Assessment (0 - 100)
    private Double midtermScore;         // Midterm Marks (0 - 100)
    private Double labPracticalScore;    // Practical / Lab evaluation (0 - 100)
    private Double pastSemesterGpa;      // 0.00 - 10.00 (Historical baseline)
    private Double weeklyStudyHours;     // Hours/week (e.g. 15.0)

    // Foundational Multi-dimensional Skill Evaluations (0 - 100)
    private Double codingSkillScore;      // Programming / Data Structures
    private Double analyticalMathScore;   // Math / Statistics / Calculus
    private Double problemSolvingScore;   // Logic / Algorithmic Thinking
    private Double communicationScore;    // Verbal, Written, Team Presentation
    private Double dataAiScore;           // Machine Learning, SQL, Big Data
    private Double cloudSystemsScore;     // Cloud, DevOps, Networks, OS
    private Double coCurricularScore;     // Hackathons, Clubs, Leadership
}
