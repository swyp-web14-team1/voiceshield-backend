package com.swyp.voiceshield.casecatalog;

public record CaseQuizResponse(
        String quizId,
        int quizNumber,
        String question
) {

    static CaseQuizResponse from(CaseVariantQuiz quiz) {
        return new CaseQuizResponse(
                quiz.getId(),
                quiz.getQuizNumber(),
                quiz.getQuestion()
        );
    }
}
