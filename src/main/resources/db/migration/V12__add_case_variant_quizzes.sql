CREATE TABLE case_variant_quizzes (
    quiz_id VARCHAR(255) PRIMARY KEY,
    variant_id VARCHAR(255) NOT NULL,
    quiz_number INTEGER NOT NULL,
    question TEXT NOT NULL,
    explanation TEXT NOT NULL,
    recommended_scenario_id VARCHAR(255),
    CONSTRAINT fk_case_variant_quizzes_variant
        FOREIGN KEY (variant_id) REFERENCES case_variants (variant_id),
    CONSTRAINT fk_case_variant_quizzes_recommended_scenario
        FOREIGN KEY (recommended_scenario_id) REFERENCES case_scenarios (scenario_id),
    CONSTRAINT uk_case_variant_quizzes_variant_number
        UNIQUE (variant_id, quiz_number)
);

INSERT INTO case_variant_quizzes (
    quiz_id,
    variant_id,
    quiz_number,
    question,
    explanation,
    recommended_scenario_id
)
VALUES (
    'case-mobile-repair-voice-quiz-1',
    'case-mobile-repair-voice',
    1,
    '다음 중 사기임을 판단할 수 있는 결정적인 단서는 무엇일까요?',
    '낯선 사람이 알려준 연락처나 계좌를 그대로 이용하지 말고, 공식 대표번호나 기존에 저장된 번호로 직접 사실 여부를 확인해야 합니다.',
    'case-return-delivery'
);
