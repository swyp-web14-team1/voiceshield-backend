package com.swyp.voiceshield.casecatalog;

public record CaseVariantOptionResponse(
        String optionId,
        int optionNumber,
        String optionText,
        boolean isCorrect
) {

    static CaseVariantOptionResponse from(CaseVariantOption option) {
        return new CaseVariantOptionResponse(
                option.getId(),
                option.getOptionNumber(),
                option.getOptionText(),
                option.isCorrect()
        );
    }
}
