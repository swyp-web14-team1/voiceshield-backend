package com.swyp.voiceshield.report;

import java.util.List;

/**
 * 다음에 학습할 사례 추천 응답.
 *
 * @param status          ALL_COMPLETED면 {@code recommendations}는 빈 배열이다
 * @param guide           화면에 그대로 출력할 안내 문구
 * @param recommendations 추천 순서대로. 프론트에서 다시 정렬하지 않아도 된다
 */
public record RecommendationResponse(
        RecommendationStatus status,
        String guide,
        List<RecommendationItem> recommendations
) {

    static RecommendationResponse of(List<RecommendationItem> recommendations) {
        if (recommendations.isEmpty()) {
            return new RecommendationResponse(
                    RecommendationStatus.ALL_COMPLETED,
                    VulnerabilityMessages.ALL_COMPLETED_GUIDE,
                    List.of()
            );
        }
        return new RecommendationResponse(
                RecommendationStatus.READY,
                VulnerabilityMessages.RECOMMENDATION_GUIDE,
                recommendations
        );
    }

    /**
     * 추천 사례 1건.
     *
     * @param reasonTypeId 추천 근거가 된 취약 유형. 근거 없이 "안 해본 사례"로 뽑혔으면 null
     * @param reason       추천 이유 한 문장. 그대로 출력하면 된다
     */
    public record RecommendationItem(
            String scenarioId,
            String title,
            String difficulty,
            String estimatedLearningTime,
            String categoryId,
            String categoryName,
            ScenarioLearningState learningState,
            String reasonTypeId,
            String reasonLabel,
            String reason
    ) {
    }
}
