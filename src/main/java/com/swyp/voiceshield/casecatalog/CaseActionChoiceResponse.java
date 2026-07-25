package com.swyp.voiceshield.casecatalog;

/**
 * 정본 '🎮 선택지' — 시뮬레이션 진행 중 사용자가 고르는 행동.
 *
 * <p>퀴즈 보기({@link CaseVariantOptionResponse})와 달리 회차({@code stepNumber})를 갖는다.
 * 정본 message 3건은 대화 중 분기가 2회다.
 */
public record CaseActionChoiceResponse(
        String choiceOptionId,
        int stepNumber,
        int optionNumber,
        String optionText,
        boolean isCorrect
) {

    static CaseActionChoiceResponse from(CaseVariantOption option) {
        return new CaseActionChoiceResponse(
                option.getId(),
                option.getStepNumber(),
                option.getOptionNumber(),
                option.getOptionText(),
                option.isCorrect()
        );
    }
}
