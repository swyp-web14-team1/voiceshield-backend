package com.swyp.voiceshield.casecatalog;

import java.util.List;

public record CaseSimulationStepResponse(
        String scenarioId,
        String variantId,
        CaseChannel channel,
        List<String> scriptLines,
        List<CaseActionChoiceResponse> actionChoices
) {

    static CaseSimulationStepResponse from(CaseScenario scenario, CaseVariant variant, List<String> scriptLines) {
        List<CaseActionChoiceResponse> actionChoices = variant.getActionChoices().stream()
                .map(CaseActionChoiceResponse::from)
                .toList();

        return new CaseSimulationStepResponse(
                scenario.getId(),
                variant.getId(),
                variant.getChannel(),
                scriptLines,
                actionChoices
        );
    }
}
