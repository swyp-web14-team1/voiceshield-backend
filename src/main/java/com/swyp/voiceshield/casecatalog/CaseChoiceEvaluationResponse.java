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

    /**
     * @param quiz 행동 선택지 채점에는 퀴즈가 없을 수 있다. 이때 해설·추천 학습은 비워 내려간다.
     */
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
                quiz == null ? null : quiz.getExplanation(),
                RecommendedLearningResponse.from(quiz == null ? null : quiz.getRecommendedScenario())
        );
    }
}
