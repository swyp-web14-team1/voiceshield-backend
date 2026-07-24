package com.swyp.voiceshield.category;

import com.swyp.voiceshield.casecatalog.CaseScenario;

public record CategoryCaseSummaryResponse(
        String scenarioId,
        String caseName,
        String difficulty,
        String estimatedLearningTime
) {

    public static CategoryCaseSummaryResponse from(CaseScenario scenario) {
        return new CategoryCaseSummaryResponse(
                scenario.getId(),
                scenario.getName(),
                scenario.getDifficulty(),
                scenario.getEstimatedLearningTime()
        );
    }
}
