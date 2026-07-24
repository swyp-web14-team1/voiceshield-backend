package com.swyp.voiceshield.casecatalog;

public record RecommendedLearningResponse(
        String scenarioId,
        String title,
        String difficulty,
        String estimatedLearningTime
) {

    static RecommendedLearningResponse from(CaseScenario scenario) {
        if (scenario == null) {
            return null;
        }

        return new RecommendedLearningResponse(
                scenario.getId(),
                scenario.getName(),
                scenario.getDifficulty(),
                scenario.getEstimatedLearningTime()
        );
    }
}
