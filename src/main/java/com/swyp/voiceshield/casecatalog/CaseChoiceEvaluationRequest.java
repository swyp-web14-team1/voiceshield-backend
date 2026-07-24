package com.swyp.voiceshield.casecatalog;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record CaseChoiceEvaluationRequest(
        String choiceOptionId,
        List<@NotBlank String> choiceOptionIds
) {

    List<String> selectedChoiceOptionIds() {
        if (choiceOptionIds != null && !choiceOptionIds.isEmpty()) {
            return choiceOptionIds;
        }
        if (choiceOptionId != null && !choiceOptionId.isBlank()) {
            return List.of(choiceOptionId);
        }
        return List.of();
    }
}
