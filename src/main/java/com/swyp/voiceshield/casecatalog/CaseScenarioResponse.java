package com.swyp.voiceshield.casecatalog;

import java.util.LinkedHashMap;
import java.util.Map;

public record CaseScenarioResponse(
        String scenarioId,
        String categoryId,
        String categoryName,
        String caseName,
        String difficulty,
        String estimatedLearningTime,
        String completionRate,
        Map<String, CaseVariantResponse> variants
) {

    static CaseScenarioResponse from(CaseScenario scenario) {
        Map<String, CaseVariantResponse> variants = new LinkedHashMap<>();
        scenario.getVariants().forEach(variant ->
                variants.put(variant.getChannel().name().toLowerCase(), CaseVariantResponse.from(variant)));
        return new CaseScenarioResponse(
                scenario.getId(),
                scenario.getCategory().getId(),
                scenario.getCategory().getName(),
                scenario.getName(),
                scenario.getDifficulty(),
                scenario.getEstimatedLearningTime(),
                scenario.getCompletionRate(),
                variants
        );
    }
}
