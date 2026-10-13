package com.predictor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerSuggestionDto {
    private String title;                // e.g. "AI & Machine Learning Engineer"
    private String category;             // e.g. "Data & Intelligence"
    private Double matchPercentage;      // e.g. 94.5%
    private String description;          // Brief role scope
    private String avgSalaryRange;       // e.g. "$115,000 - $165,000 / yr"
    private String marketDemand;         // "Exponential Growth" / "Very High" / "Stable"
    private List<String> keyStrengths;   // Key aligned skills student already has
    private List<String> skillGaps;      // Skills to level up
    private List<String> recommendedCerts; // e.g. ["AWS ML Specialty", "TensorFlow Developer"]
    private List<String> actionSteps;    // High-impact next steps
}
