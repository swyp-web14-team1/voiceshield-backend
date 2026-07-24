package com.swyp.voiceshield.learning;

import java.time.LocalDateTime;
import java.util.List;

public record LearningOverviewResponse(
        long completedScenarioCount,
        long totalScenarioCount,
        int progressPercentage,
        List<RecentLearningItem> recentLearning
) {

    public static LearningOverviewResponse from(
            long completedScenarioCount,
            long totalScenarioCount,
            List<RecentLearningItem> recentLearning
    ) {
        int progressPercentage = totalScenarioCount == 0
                ? 0
                : (int) ((completedScenarioCount * 100) / totalScenarioCount);

        return new LearningOverviewResponse(
                completedScenarioCount,
                totalScenarioCount,
                progressPercentage,
                recentLearning
        );
    }

    public record RecentLearningItem(
            String scenarioId,
            String title,
            String categoryId,
            String categoryName,
            LearningStatus status,
            LocalDateTime lastLearnedAt
    ) {

        public static RecentLearningItem from(LearningHistory learningHistory) {
            return new RecentLearningItem(
                    learningHistory.getScenario().getId(),
                    learningHistory.getScenario().getName(),
                    learningHistory.getScenario().getCategory().getId(),
                    learningHistory.getScenario().getCategory().getName(),
                    learningHistory.getStatus(),
                    learningHistory.getUpdatedAt()
            );
        }
    }
}
