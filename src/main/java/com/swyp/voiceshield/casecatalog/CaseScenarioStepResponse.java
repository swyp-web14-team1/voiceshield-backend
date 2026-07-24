package com.swyp.voiceshield.casecatalog;

import java.util.Comparator;
import java.util.List;

public record CaseScenarioStepResponse(
        String scenarioId,
        String variantId,
        CaseChannel channel,
        CaseQuizResponse quiz,
        List<String> scriptLines,
        List<CaseVariantOptionResponse> choices
) {

    static CaseScenarioStepResponse from(CaseScenario scenario, CaseVariant variant, List<String> scriptLines) {
        List<CaseVariantOptionResponse> choices = variant.getOptions().stream()
                .sorted(Comparator.comparingInt(CaseVariantOption::getOptionNumber))
                .map(CaseVariantOptionResponse::from)
                .toList();

        return new CaseScenarioStepResponse(
                scenario.getId(),
                variant.getId(),
                variant.getChannel(),
                CaseQuizResponse.from(variant.getQuiz()),
                scriptLines,
                choices
        );
    }
}
