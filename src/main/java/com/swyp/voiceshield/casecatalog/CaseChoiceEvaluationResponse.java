package com.swyp.voiceshield.casecatalog;

public record CaseChoiceEvaluationResponse(
        String choiceOptionId,
        int optionNumber,
        boolean isCorrect
) {

    static CaseChoiceEvaluationResponse from(CaseVariantOption option) {
        return new CaseChoiceEvaluationResponse(
                option.getId(),
                option.getOptionNumber(),
                option.isCorrect()
        );
    }
}
