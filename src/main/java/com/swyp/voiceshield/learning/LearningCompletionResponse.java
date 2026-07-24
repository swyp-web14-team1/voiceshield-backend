package com.swyp.voiceshield.learning;

public record LearningCompletionResponse(
        String scenarioId,
        String status,
        boolean completed,
        String message
) {

    public static LearningCompletionResponse inProgress(String scenarioId) {
        return new LearningCompletionResponse(
                scenarioId,
                LearningStatus.IN_PROGRESS.name(),
                false,
                "시뮬레이션 완료 상태가 저장되었습니다."
        );
    }

    public static LearningCompletionResponse alreadyComplete(String scenarioId) {
        return new LearningCompletionResponse(
                scenarioId,
                LearningStatus.COMPLETE.name(),
                true,
                "이미 최종 완료된 학습입니다."
        );
    }

    public static LearningCompletionResponse complete(String scenarioId) {
        return new LearningCompletionResponse(
                scenarioId,
                LearningStatus.COMPLETE.name(),
                true,
                "학습이 최종 완료 처리되었습니다."
        );
    }
}
