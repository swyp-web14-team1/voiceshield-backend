package com.swyp.voiceshield.casecatalog;

import jakarta.validation.constraints.NotBlank;

public record CaseChoiceEvaluationRequest(
        @NotBlank String choiceOptionId
) {
}
