package com.predictor.ml;

import com.predictor.dto.CareerSuggestionDto;
import com.predictor.dto.PredictionRequestDto;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Career Matching Algorithm based on Multi-Dimensional Cosine Similarity & Weighted Competency Alignment.
 */
@Component
public class CareerMatchingAlgorithm {

    // Predefined Career Benchmark Archetypes (Weights: Coding, Math, Problem Solving, Comm, DataAI, Cloud, CoCurricular)
    private static class CareerBenchmark {
        String title;
        String category;
        String description;
        String salary;
        String demand;
        double[] weights; // Normalized vector of required proficiencies (0.0 - 1.0)
        List<String> certifications;
        List<String> coreSkills;

        public CareerBenchmark(String title, String category, String description, String salary, String demand,
                               double[] weights, List<String> certs, List<String> coreSkills) {
            this.title = title;
            this.category = category;
            this.description = description;
            this.salary = salary;
            this.demand = demand;
            this.weights = weights;
            this.certifications = certs;
            this.coreSkills = coreSkills;
        }
    }

    private final List<CareerBenchmark> benchmarks = new ArrayList<>();

    public CareerMatchingAlgorithm() {
        // 1. AI & Machine Learning Engineer
        benchmarks.add(new CareerBenchmark(
                "AI & Machine Learning Engineer",
                "Artificial Intelligence & Data",
                "Designs and deploys deep learning models, generative AI architectures, and neural algorithms.",
                "$125,000 - $185,000 / yr",
                "Exponential Growth (38% YoY)",
                new double[]{0.85, 0.90, 0.85, 0.60, 0.95, 0.70, 0.50},
                Arrays.asList("TensorFlow Developer Certificate", "AWS Certified Machine Learning - Specialty", "DeepLearning.AI Spec"),
                Arrays.asList("Data & AI", "Analytical Mathematics", "Problem Solving")
        ));

        // 2. Full-Stack Software Engineer
        benchmarks.add(new CareerBenchmark(
                "Full-Stack Software Engineer",
                "Software Engineering",
                "Engineers scalable end-to-end web architectures, microservices, and reactive user interfaces.",
                "$105,000 - $155,000 / yr",
                "Very High Demand",
                new double[]{0.90, 0.60, 0.85, 0.75, 0.50, 0.80, 0.65},
                Arrays.asList("AWS Certified Developer Associate", "Meta Front-End & Back-End Professional", "Spring Certified Professional"),
                Arrays.asList("Coding Proficiency", "Cloud & Systems", "Problem Solving")
        ));

        // 3. Cloud DevOps & Solutions Architect
        benchmarks.add(new CareerBenchmark(
                "Cloud Solutions & DevOps Architect",
                "Cloud & Infrastructure",
                "Orchestrates automated CI/CD pipelines, containerized clusters, and fault-tolerant cloud infrastructures.",
                "$120,000 - $175,000 / yr",
                "High Demand (Mission-Critical)",
                new double[]{0.70, 0.55, 0.80, 0.70, 0.50, 0.95, 0.60},
                Arrays.asList("AWS Solutions Architect Professional", "Certified Kubernetes Administrator (CKA)", "HashiCorp Terraform Associate"),
                Arrays.asList("Cloud & Systems", "Problem Solving", "Coding Proficiency")
        ));

        // 4. Data Scientist & Analytics Specialist
        benchmarks.add(new CareerBenchmark(
                "Data Scientist & Quantitative Analyst",
                "Data Analytics & Insights",
                "Transforms multi-dimensional statistical data into predictive insights, business intelligence, and ML pipelines.",
                "$110,000 - $160,000 / yr",
                "High & Expanding",
                new double[]{0.75, 0.90, 0.85, 0.70, 0.85, 0.55, 0.50},
                Arrays.asList("Google Professional Data Engineer", "IBM Data Science Professional", "SAS Certified Data Scientist"),
                Arrays.asList("Analytical Mathematics", "Data & AI", "Problem Solving")
        ));

        // 5. Cybersecurity & Information Assurance Analyst
        benchmarks.add(new CareerBenchmark(
                "Cybersecurity & Threat Analyst",
                "Security & Networks",
                "Shields enterprise systems through vulnerability auditing, zero-trust architectures, and incident triage.",
                "$110,000 - $165,000 / yr",
                "Critical Global Shortage",
                new double[]{0.70, 0.65, 0.85, 0.65, 0.55, 0.85, 0.50},
                Arrays.asList("CompTIA Security+", "Certified Ethical Hacker (CEH)", "CISSP Associate"),
                Arrays.asList("Cloud & Systems", "Problem Solving", "Coding Proficiency")
        ));

        // 6. Technical Product & Agile Delivery Manager
        benchmarks.add(new CareerBenchmark(
                "Technical Product & Solutions Manager",
                "Leadership & Product Strategy",
                "Coordinates cross-functional engineering teams, technical roadmaps, and customer-driven software delivery.",
                "$115,000 - $170,000 / yr",
                "Steady High Growth",
                new double[]{0.55, 0.60, 0.75, 0.95, 0.60, 0.65, 0.90},
                Arrays.asList("Certified Scrum Product Owner (CSPO)", "Pragmatic Institute Certified (PMC)", "PMP / CAPM"),
                Arrays.asList("Communication", "Co-Curricular / Leadership", "Problem Solving")
        ));

        // 7. Mobile App Developer (iOS / Android)
        benchmarks.add(new CareerBenchmark(
                "Mobile Applications Developer",
                "Mobile Systems",
                "Architects performant cross-platform and native iOS/Android mobile applications.",
                "$100,000 - $150,000 / yr",
                "Solid Demand",
                new double[]{0.85, 0.50, 0.75, 0.70, 0.45, 0.65, 0.60},
                Arrays.asList("Meta React Native Specialization", "Google Associate Android Developer", "Apple Swift Certification"),
                Arrays.asList("Coding Proficiency", "Problem Solving", "Communication")
        ));
    }

    public List<CareerSuggestionDto> matchCareers(PredictionRequestDto req) {
        // Extract student normalized skills vector
        double coding = (req.getCodingSkillScore() != null ? req.getCodingSkillScore() : 60.0) / 100.0;
        double math = (req.getAnalyticalMathScore() != null ? req.getAnalyticalMathScore() : 60.0) / 100.0;
        double problem = (req.getProblemSolvingScore() != null ? req.getProblemSolvingScore() : 60.0) / 100.0;
        double comm = (req.getCommunicationScore() != null ? req.getCommunicationScore() : 60.0) / 100.0;
        double dataAi = (req.getDataAiScore() != null ? req.getDataAiScore() : 60.0) / 100.0;
        double cloud = (req.getCloudSystemsScore() != null ? req.getCloudSystemsScore() : 60.0) / 100.0;
        double coCurricular = (req.getCoCurricularScore() != null ? req.getCoCurricularScore() : 60.0) / 100.0;

        double[] studentVector = new double[]{coding, math, problem, comm, dataAi, cloud, coCurricular};
        String[] skillLabels = new String[]{"Coding Proficiency", "Analytical Mathematics", "Problem Solving", "Communication", "Data & AI", "Cloud & Systems", "Co-Curricular & Teamwork"};

        List<CareerSuggestionDto> results = new ArrayList<>();

        for (CareerBenchmark bm : benchmarks) {
            // Calculate Weighted Proximity Score
            double dotProduct = 0.0;
            double normStudent = 0.0;
            double normBenchmark = 0.0;
            double weightedError = 0.0;

            for (int i = 0; i < 7; i++) {
                dotProduct += studentVector[i] * bm.weights[i];
                normStudent += Math.pow(studentVector[i], 2);
                normBenchmark += Math.pow(bm.weights[i], 2);

                double diff = studentVector[i] - bm.weights[i];
                weightedError += bm.weights[i] * (diff < 0 ? Math.abs(diff) * 1.2 : Math.abs(diff) * 0.4);
            }

            normStudent = Math.sqrt(normStudent);
            normBenchmark = Math.sqrt(normBenchmark);

            double cosine = (normStudent > 0 && normBenchmark > 0) ? (dotProduct / (normStudent * normBenchmark)) : 0.5;
            
            // Scaled Match Percentage
            double penaltyAdjusted = Math.max(0.0, 1.0 - (weightedError / 3.0));
            double compositeMatch = (cosine * 0.55 + penaltyAdjusted * 0.45) * 100.0;
            double matchPercentage = Math.round(Math.min(98.8, Math.max(45.0, compositeMatch)) * 10.0) / 10.0;

            // Extract Identified Strengths and Skill Gaps
            List<String> strengths = new ArrayList<>();
            List<String> gaps = new ArrayList<>();

            for (int i = 0; i < 7; i++) {
                if (studentVector[i] >= (bm.weights[i] - 0.08) && studentVector[i] >= 0.60) {
                    strengths.add(skillLabels[i] + " (" + Math.round(studentVector[i] * 100) + "/100)");
                } else if (studentVector[i] < bm.weights[i] - 0.12) {
                    gaps.add(skillLabels[i] + " (Current: " + Math.round(studentVector[i] * 100) + "/100, Target: " + Math.round(bm.weights[i] * 100) + "/100)");
                }
            }

            if (strengths.isEmpty()) strengths.add("Foundational Learning Potential");
            if (gaps.isEmpty()) gaps.add("Sustain portfolio excellence & advanced project work");

            // Action Steps
            List<String> actionSteps = Arrays.asList(
                    "Build 2 production-ready capstone portfolio projects targeting " + bm.title,
                    "Acquire verified certification in " + bm.certifications.get(0),
                    "Participate in technical interview preparation focusing on " + bm.coreSkills.get(0)
            );

            results.add(CareerSuggestionDto.builder()
                    .title(bm.title)
                    .category(bm.category)
                    .matchPercentage(matchPercentage)
                    .description(bm.description)
                    .avgSalaryRange(bm.salary)
                    .marketDemand(bm.demand)
                    .keyStrengths(strengths)
                    .skillGaps(gaps)
                    .recommendedCerts(bm.certifications)
                    .actionSteps(actionSteps)
                    .build());
        }

        // Sort descending by match percentage
        results.sort((a, b) -> Double.compare(b.getMatchPercentage(), a.getMatchPercentage()));
        return results;
    }
}
