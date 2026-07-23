package com.swyp.voiceshield.casecatalog;

public record CaseChoiceResultOptionResponse(
        String optionId,
        int optionNumber,
        String optionText,
        boolean isCorrect
) {

    static CaseChoiceResultOptionResponse from(CaseVariantOption option) {
        return new CaseChoiceResultOptionResponse(
                option.getId(),
                option.getOptionNumber(),
                option.getOptionText(),
                option.isCorrect()
        );
    }
}
