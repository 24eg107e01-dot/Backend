package com.predictor.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predictor.dto.CareerSuggestionDto;
import com.predictor.entity.AcademicAlert;
import com.predictor.entity.PredictionHistory;
import com.predictor.entity.User;
import com.predictor.repository.AcademicAlertRepository;
import com.predictor.repository.PredictionHistoryRepository;
import com.predictor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final AcademicAlertRepository alertRepo;
    private final PredictionHistoryRepository predictionRepo;
    private final PasswordEncoder passwordEncoder;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) {
        if (userRepo.count() > 0) return;

        log.info("Initializing demo seed data for Student Performance & Career Predictor...");

        // 1. Seed Advisor Account
        User advisor = User.builder()
                .email("advisor@university.edu")
                .password(passwordEncoder.encode("advisor123"))
                .fullName("Dr. Evelyn Vance")
                .role(User.Role.ADVISOR)
                .department("Academic Affairs & Computer Science")
                .build();
        userRepo.save(advisor);

        // 2. Seed Student Account
        User student = User.builder()
                .email("student@university.edu")
                .password(passwordEncoder.encode("student123"))
                .fullName("Alex Rivera")
                .role(User.Role.STUDENT)
                .studentRollNo("CS-2026-104")
                .department("Computer Science & Engineering")
                .build();
        userRepo.save(student);

        // 3. Seed Sample Academic Alerts for Early Intervention Portal
        AcademicAlert a1 = AcademicAlert.builder()
                .rollNumber("CS-2026-042")
                .studentName("Marcus Sterling")
                .studentEmail("marcus.s@university.edu")
                .department("Computer Science")
                .semester(4)
                .riskLevel(PredictionHistory.RiskLevel.CRITICAL)
                .predictedGpa(4.85)
                .attendancePercentage(61.5)
                .triggerReason("Attendance 61.5% is critically below mandatory threshold. Continuous assessment failure in Algorithms.")
                .recommendedInterventionPlan("Mandatory meeting scheduled with student. Assign peer mentor for DSA.")
                .status(AcademicAlert.AlertStatus.OPEN)
                .createdAt(LocalDateTime.now().minusDays(2))
                .build();

        AcademicAlert a2 = AcademicAlert.builder()
                .rollNumber("AI-2026-119")
                .studentName("Sophia Chen")
                .studentEmail("sophia.c@university.edu")
                .department("Artificial Intelligence")
                .semester(6)
                .riskLevel(PredictionHistory.RiskLevel.MODERATE)
                .predictedGpa(6.40)
                .attendancePercentage(76.2)
                .triggerReason("Midterm exam drop in Linear Algebra (-18%). Lab evaluations inconsistent.")
                .recommendedInterventionPlan("Provide tutorial access on Vector Calculus and attend lab remedial.")
                .status(AcademicAlert.AlertStatus.IN_REVIEW)
                .counselorNotes("Student was unwell during midterm week. Follow-up meeting next Tuesday.")
                .createdAt(LocalDateTime.now().minusDays(4))
                .build();

        AcademicAlert a3 = AcademicAlert.builder()
                .rollNumber("DS-2026-088")
                .studentName("Liam O'Connor")
                .studentEmail("liam.o@university.edu")
                .department("Data Science")
                .semester(3)
                .riskLevel(PredictionHistory.RiskLevel.CRITICAL)
                .predictedGpa(5.10)
                .attendancePercentage(58.0)
                .triggerReason("Attendance deficit (58%). Missed 2 continuous evaluation submissions.")
                .recommendedInterventionPlan("Notify academic dean and academic advisor. Implement weekly log.")
                .status(AcademicAlert.AlertStatus.OPEN)
                .createdAt(LocalDateTime.now().minusHours(8))
                .build();

        AcademicAlert a4 = AcademicAlert.builder()
                .rollNumber("SE-2026-015")
                .studentName("Aria Montgomery")
                .studentEmail("aria.m@university.edu")
                .department("Software Engineering")
                .semester(5)
                .riskLevel(PredictionHistory.RiskLevel.SAFE)
                .predictedGpa(8.95)
                .attendancePercentage(94.0)
                .triggerReason("High achiever milestone reached. Recommended for elite career fast-track.")
                .recommendedInterventionPlan("Nominate for Industry Cloud Fellowship & Research Internship.")
                .status(AcademicAlert.AlertStatus.RESOLVED)
                .counselorNotes("Awarded nomination letter for Summer Research Fellowship.")
                .createdAt(LocalDateTime.now().minusDays(7))
                .resolvedAt(LocalDateTime.now().minusDays(3))
                .build();

        alertRepo.saveAll(Arrays.asList(a1, a2, a3, a4));

        // 4. Seed Initial Prediction History
        try {
            List<String> bottlenecks = Arrays.asList("Attendance below 75%", "Internal mark vulnerability");
            String bottlenecksJson = objectMapper.writeValueAsString(bottlenecks);

            List<CareerSuggestionDto> sampleCareers = Arrays.asList(
                    CareerSuggestionDto.builder()
                            .title("Full-Stack Software Engineer")
                            .category("Software Engineering")
                            .matchPercentage(93.4)
                            .avgSalaryRange("$105,000 - $155,000 / yr")
                            .marketDemand("Very High Demand")
                            .keyStrengths(Arrays.asList("Coding Proficiency", "Problem Solving"))
                            .skillGaps(Arrays.asList("Cloud & Systems"))
                            .recommendedCerts(Arrays.asList("AWS Certified Developer"))
                            .actionSteps(Arrays.asList("Build microservices capstone project"))
                            .build()
            );
            String careersJson = objectMapper.writeValueAsString(sampleCareers);

            PredictionHistory ph1 = PredictionHistory.builder()
                    .user(student)
                    .rollNumber("CS-2026-104")
                    .studentName("Alex Rivera")
                    .predictedGpa(8.45)
                    .predictedGpaMin(8.17)
                    .predictedGpaMax(8.73)
                    .confidenceScore(94.6)
                    .riskLevel(PredictionHistory.RiskLevel.SAFE)
                    .riskScore(12.0)
                    .riskFactorsJson(bottlenecksJson)
                    .recommendedCareersJson(careersJson)
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();

            PredictionHistory ph2 = PredictionHistory.builder()
                    .user(null)
                    .rollNumber("CS-2026-042")
                    .studentName("Marcus Sterling")
                    .predictedGpa(4.85)
                    .predictedGpaMin(4.55)
                    .predictedGpaMax(5.15)
                    .confidenceScore(88.2)
                    .riskLevel(PredictionHistory.RiskLevel.CRITICAL)
                    .riskScore(78.5)
                    .riskFactorsJson(bottlenecksJson)
                    .recommendedCareersJson(careersJson)
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();

            predictionRepo.saveAll(Arrays.asList(ph1, ph2));
        } catch (Exception e) {
            log.error("Error seeding prediction history", e);
        }

        log.info("Demo seed data initialization complete.");
    }
}
