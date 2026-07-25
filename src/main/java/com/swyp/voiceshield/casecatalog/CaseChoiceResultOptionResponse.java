package com.swyp.voiceshield.casecatalog;

/**
 * 채점 결과에 실리는 선택지.
 *
 * <p>{@code stepNumber} 는 행동 선택지의 회차다. 회차가 2회인 정본 message 3건은
 * 회차가 달라도 {@code optionNumber} 가 겹치므로, 이 값이 없으면 결과를 특정할 수 없다.
 * 퀴즈 보기는 항상 1이다.
 */
public record CaseChoiceResultOptionResponse(
        String optionId,
        int stepNumber,
        int optionNumber,
        String optionText,
        boolean isCorrect
) {

    static CaseChoiceResultOptionResponse from(CaseVariantOption option) {
        return new CaseChoiceResultOptionResponse(
                option.getId(),
                option.getStepNumber(),
                option.getOptionNumber(),
                option.getOptionText(),
                option.isCorrect()
        );
    }
}
