package com.predictor.service;

import com.predictor.dto.CareerSuggestionDto;
import com.predictor.dto.PredictionRequestDto;
import com.predictor.dto.PredictionResponseDto;
import com.predictor.ml.CareerMatchingAlgorithm;
import com.predictor.ml.EnsembleRandomForest;
import com.predictor.ml.MultiLayerPerceptronEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MachineLearningService {

    private final EnsembleRandomForest randomForest;
    private final MultiLayerPerceptronEngine mlpEngine;
    private final CareerMatchingAlgorithm careerMatcher;

    @Value("${app.ml.python-service-url:http://localhost:8000}")
    private String pythonServiceUrl;

    public PredictionResponseDto runFullPrediction(PredictionRequestDto req) {
        // 1. Run Random Forest Bagging Ensemble
        EnsembleRandomForest.ForestResult rfResult = randomForest.predict(req);

        // 2. Run Deep Multi-Layer Perceptron (MLP) Neural Network
        double mlpGpa = mlpEngine.predict(req);

        // 3. Ensemble Blending (60% Random Forest + 40% MLP Neural Net)
        double blendedGpa = Math.round(((rfResult.predictedGpa * 0.60) + (mlpGpa * 0.40)) * 100.0) / 100.0;
        blendedGpa = Math.max(2.0, Math.min(10.0, blendedGpa));

        double marginOfError = 0.28;
        double minGpa = Math.max(0.0, Math.round((blendedGpa - marginOfError) * 100.0) / 100.0);
        double maxGpa = Math.min(10.0, Math.round((blendedGpa + marginOfError) * 100.0) / 100.0);

        // 4. Calculate Letter Grade
        String grade;
        if (blendedGpa >= 9.0) grade = "O (Outstanding)";
        else if (blendedGpa >= 8.0) grade = "A+ (Excellent)";
        else if (blendedGpa >= 7.0) grade = "A (Very Good)";
        else if (blendedGpa >= 6.0) grade = "B+ (Good)";
        else if (blendedGpa >= 5.0) grade = "B (Above Average)";
        else if (blendedGpa >= 4.0) grade = "C (Pass)";
        else grade = "F (Academic Distress)";

        // 5. Determine Academic Risk Level
        String riskLevel;
        boolean earlyIntervention = false;
        String interventionMessage = "";

        if (rfResult.riskScore >= 60.0 || blendedGpa < 5.5 || (req.getAttendancePercentage() != null && req.getAttendancePercentage() < 75.0)) {
            riskLevel = "CRITICAL";
            earlyIntervention = true;
            interventionMessage = "CRITICAL RISK FLAGGED: Automated advisory alert dispatched. Academic retention intervention recommended.";
        } else if (rfResult.riskScore >= 30.0 || blendedGpa < 7.0) {
            riskLevel = "MODERATE";
            interventionMessage = "MODERATE RISK: Monitoring recommended. Targeted tutoring on identified bottlenecks suggested.";
        } else {
            riskLevel = "SAFE";
            interventionMessage = "EXCELLENT TRAJECTORY: Student demonstrates strong academic consistency and solid mastery.";
        }

        // 6. Career Matching Engine
        List<CareerSuggestionDto> topCareers = careerMatcher.matchCareers(req);

        // 7. Radar Skill Scores for Visualization
        Map<String, Double> radarScores = new LinkedHashMap<>();
        radarScores.put("Coding / DSA", req.getCodingSkillScore() != null ? req.getCodingSkillScore() : 60.0);
        radarScores.put("Mathematics", req.getAnalyticalMathScore() != null ? req.getAnalyticalMathScore() : 60.0);
        radarScores.put("Problem Solving", req.getProblemSolvingScore() != null ? req.getProblemSolvingScore() : 60.0);
        radarScores.put("Communication", req.getCommunicationScore() != null ? req.getCommunicationScore() : 60.0);
        radarScores.put("Data & AI", req.getDataAiScore() != null ? req.getDataAiScore() : 60.0);
        radarScores.put("Cloud / Systems", req.getCloudSystemsScore() != null ? req.getCloudSystemsScore() : 60.0);
        radarScores.put("Co-Curricular", req.getCoCurricularScore() != null ? req.getCoCurricularScore() : 60.0);

        // 8. Personalized Advice
        List<String> advice = new ArrayList<>();
        if (req.getAttendancePercentage() != null && req.getAttendancePercentage() < 80.0) {
            advice.add("Increase attendance to >= 85% to mitigate direct internal assessment deductions.");
        }
        if (req.getWeeklyStudyHours() != null && req.getWeeklyStudyHours() < 12.0) {
            advice.add("Target 15+ focused study hours weekly using the Pomodoro technique to boost GPA by up to 0.8.");
        }
        advice.add("Leverage top recommended career track: " + topCareers.get(0).getTitle() + " (" + topCareers.get(0).getMatchPercentage() + "% match).");
        advice.add("Complete portfolio milestone: " + topCareers.get(0).getActionSteps().get(0));

        return PredictionResponseDto.builder()
                .predictedGpa(blendedGpa)
                .predictedGpaMin(minGpa)
                .predictedGpaMax(maxGpa)
                .predictedGrade(grade)
                .confidenceScore(rfResult.confidence)
                .riskLevel(riskLevel)
                .riskScore(rfResult.riskScore)
                .riskFactors(rfResult.bottlenecks)
                .earlyInterventionTriggered(earlyIntervention)
                .earlyInterventionMessage(interventionMessage)
                .topCareerRecommendations(topCareers)
                .modelUsed("Hybrid Ensemble: Random Forest (50 Bagged Trees) + Multi-Layer Perceptron (Deep ReLU ANN)")
                .radarSkillScores(radarScores)
                .personalizedAdvice(advice)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
