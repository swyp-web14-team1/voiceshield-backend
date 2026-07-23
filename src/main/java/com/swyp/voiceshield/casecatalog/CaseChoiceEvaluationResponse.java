package com.swyp.voiceshield.casecatalog;

public record CaseChoiceEvaluationResponse(
        String choiceOptionId,
        int optionNumber,
        boolean isCorrect,
        CaseQuizResponse quiz,
        CaseChoiceResultOptionResponse selectedOption,
        CaseChoiceResultOptionResponse correctOption,
        String explanation,
        RecommendedLearningResponse recommendedLearning
) {

    static CaseChoiceEvaluationResponse from(
            CaseVariantQuiz quiz,
            CaseVariantOption selectedOption,
            CaseVariantOption correctOption
    ) {
        return new CaseChoiceEvaluationResponse(
                selectedOption.getId(),
                selectedOption.getOptionNumber(),
                selectedOption.isCorrect(),
                CaseQuizResponse.from(quiz),
                CaseChoiceResultOptionResponse.from(selectedOption),
                CaseChoiceResultOptionResponse.from(correctOption),
                quiz.getExplanation(),
                RecommendedLearningResponse.from(quiz.getRecommendedScenario())
        );
    }
}
