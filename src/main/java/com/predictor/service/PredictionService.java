package com.predictor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.predictor.dto.PredictionRequestDto;
import com.predictor.dto.PredictionResponseDto;
import com.predictor.entity.AcademicAlert;
import com.predictor.entity.PredictionHistory;
import com.predictor.entity.User;
import com.predictor.repository.AcademicAlertRepository;
import com.predictor.repository.PredictionHistoryRepository;
import com.predictor.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PredictionService {

    private final MachineLearningService mlService;
    private final PredictionHistoryRepository predictionRepo;
    private final AcademicAlertRepository alertRepo;
    private final UserRepository userRepo;
    private final ObjectMapper objectMapper;

    @Transactional
    public PredictionResponseDto predictAndPersist(PredictionRequestDto req, String userEmail) {
        // 1. Run ML Computation
        PredictionResponseDto response = mlService.runFullPrediction(req);

        // 2. Fetch User if authenticated
        User user = null;
        if (userEmail != null && !userEmail.isBlank()) {
            Optional<User> uOpt = userRepo.findByEmail(userEmail);
            if (uOpt.isPresent()) {
                user = uOpt.get();
            }
        }

        // 3. Persist to PredictionHistory
        try {
            String riskFactorsJson = objectMapper.writeValueAsString(response.getRiskFactors());
            String careersJson = objectMapper.writeValueAsString(response.getTopCareerRecommendations());
            String inputJson = objectMapper.writeValueAsString(req);

            PredictionHistory history = PredictionHistory.builder()
                    .user(user)
                    .rollNumber(req.getRollNumber() != null ? req.getRollNumber() : (user != null ? user.getStudentRollNo() : "GUEST-STU"))
                    .studentName(req.getStudentName() != null ? req.getStudentName() : (user != null ? user.getFullName() : "Demo Student"))
                    .predictedGpa(response.getPredictedGpa())
                    .predictedGpaMin(response.getPredictedGpaMin())
                    .predictedGpaMax(response.getPredictedGpaMax())
                    .confidenceScore(response.getConfidenceScore())
                    .riskLevel(PredictionHistory.RiskLevel.valueOf(response.getRiskLevel()))
                    .riskScore(response.getRiskScore())
                    .riskFactorsJson(riskFactorsJson)
                    .recommendedCareersJson(careersJson)
                    .inputSnapshotJson(inputJson)
                    .createdAt(LocalDateTime.now())
                    .build();

            predictionRepo.save(history);
        } catch (Exception e) {
            log.error("Failed to serialize or save prediction record", e);
        }

        // 4. Automated Early Intervention Trigger
        if (response.isEarlyInterventionTriggered() || "CRITICAL".equalsIgnoreCase(response.getRiskLevel())) {
            createOrUpdateAcademicAlert(req, response, user);
        }

        return response;
    }

    private void createOrUpdateAcademicAlert(PredictionRequestDto req, PredictionResponseDto response, User user) {
        String roll = req.getRollNumber() != null ? req.getRollNumber() : (user != null ? user.getStudentRollNo() : "REG-2026-X");
        String name = req.getStudentName() != null ? req.getStudentName() : (user != null ? user.getFullName() : "Anonymous Student");
        String email = user != null ? user.getEmail() : "student." + roll.toLowerCase() + "@university.edu";

        String reason = String.join(" | ", response.getRiskFactors());
        if (reason.isBlank()) {
            reason = "Predicted GPA of " + response.getPredictedGpa() + " is below target threshold.";
        }

        String interventionPlan = "Immediate advisor one-on-one conference. Assignment of remedial tutor for technical gaps. Attendance tracking mandate.";

        AcademicAlert alert = AcademicAlert.builder()
                .rollNumber(roll)
                .studentName(name)
                .studentEmail(email)
                .department(req.getDepartment() != null ? req.getDepartment() : "Computer Science & Engineering")
                .semester(req.getSemester() != null ? req.getSemester() : 4)
                .riskLevel(PredictionHistory.RiskLevel.valueOf(response.getRiskLevel()))
                .predictedGpa(response.getPredictedGpa())
                .attendancePercentage(req.getAttendancePercentage() != null ? req.getAttendancePercentage() : 65.0)
                .triggerReason(reason)
                .recommendedInterventionPlan(interventionPlan)
                .status(AcademicAlert.AlertStatus.OPEN)
                .createdAt(LocalDateTime.now())
                .build();

        alertRepo.save(alert);
        log.info("Early Intervention Alert generated for student {} ({})", name, roll);
    }

    public List<PredictionHistory> getRecentPredictions() {
        return predictionRepo.findTop10ByOrderByCreatedAtDesc();
    }
}
