package com.swyp.voiceshield.casecatalog;

import java.util.List;

public record CaseChoiceEvaluationResponse(
        String choiceOptionId,
        List<String> choiceOptionIds,
        int optionNumber,
        boolean isCorrect,
        CaseQuizResponse quiz,
        CaseChoiceResultOptionResponse selectedOption,
        List<CaseChoiceResultOptionResponse> selectedOptions,
        CaseChoiceResultOptionResponse correctOption,
        List<CaseChoiceResultOptionResponse> correctOptions,
        String explanation,
        RecommendedLearningResponse recommendedLearning
) {

    static CaseChoiceEvaluationResponse from(
            CaseVariantQuiz quiz,
            List<CaseVariantOption> selectedOptions,
            List<CaseVariantOption> correctOptions,
            boolean correct
    ) {
        CaseVariantOption firstSelectedOption = selectedOptions.get(0);
        CaseVariantOption firstCorrectOption = correctOptions.get(0);
        return new CaseChoiceEvaluationResponse(
                firstSelectedOption.getId(),
                selectedOptions.stream().map(CaseVariantOption::getId).toList(),
                firstSelectedOption.getOptionNumber(),
                correct,
                CaseQuizResponse.from(quiz),
                CaseChoiceResultOptionResponse.from(firstSelectedOption),
                selectedOptions.stream().map(CaseChoiceResultOptionResponse::from).toList(),
                CaseChoiceResultOptionResponse.from(firstCorrectOption),
                correctOptions.stream().map(CaseChoiceResultOptionResponse::from).toList(),
                quiz.getExplanation(),
                RecommendedLearningResponse.from(quiz.getRecommendedScenario())
        );
    }
}
