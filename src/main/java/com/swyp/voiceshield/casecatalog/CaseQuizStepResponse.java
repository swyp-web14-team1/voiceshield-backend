package com.swyp.voiceshield.casecatalog;

import java.util.Comparator;
import java.util.List;

public record CaseQuizStepResponse(
        String scenarioId,
        String variantId,
        CaseChannel channel,
        CaseQuizResponse quiz,
        List<CaseVariantOptionResponse> choices
) {

    static CaseQuizStepResponse from(CaseScenario scenario, CaseVariant variant) {
        List<CaseVariantOptionResponse> choices = variant.getOptions().stream()
                .sorted(Comparator.comparingInt(CaseVariantOption::getOptionNumber))
                .map(CaseVariantOptionResponse::from)
                .toList();

        return new CaseQuizStepResponse(
                scenario.getId(),
                variant.getId(),
                variant.getChannel(),
                CaseQuizResponse.from(variant.getQuiz()),
                choices
        );
    }
}
