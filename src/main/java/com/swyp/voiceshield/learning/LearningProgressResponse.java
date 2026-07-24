package com.swyp.voiceshield.learning;

public record LearningProgressResponse(
        long completedScenarioCount,
        long totalScenarioCount,
        int progressPercentage
) {

    public static LearningProgressResponse from(long completedScenarioCount, long totalScenarioCount) {
        int progressPercentage = totalScenarioCount == 0
                ? 0
                : (int) ((completedScenarioCount * 100) / totalScenarioCount);

        return new LearningProgressResponse(completedScenarioCount, totalScenarioCount, progressPercentage);
    }
}
