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
VALUES
    (
        'case-mobile-repair-voice-quiz-1',
        'case-mobile-repair-voice',
        1,
        '다음 중 사기임을 가장 강하게 의심할 수 있는 단서는 무엇일까요?',
        '휴대폰 고장이나 병원 치료는 실제로 발생할 수 있는 상황이며, 간호사가 기다리고 있다는 말도 긴급한 상황에서는 있을 수 있습니다. 하지만 전화로 개인 계좌에 즉시 송금을 요구하는 것은 가족 사칭 보이스피싱의 대표적인 수법입니다. 이런 전화를 받았다면 송금하기 전에 기존에 저장된 가족 연락처로 직접 확인하는 것이 가장 안전한 대응입니다.',
        'case-return-delivery'
    ),
    (
        'case-mobile-repair-message-quiz-1',
        'case-mobile-repair-message',
        1,
        '다음 중 가족 사칭 메신저 피싱을 가장 강하게 의심해야 하는 상황은 무엇일까요?',
        '새로운 번호로 연락하거나, 다쳤다고 말하는 것은 실제 상황에서도 발생할 수 있습니다. 하지만 개인 계좌로 즉시 송금을 요구하는 행동은 가족 사칭 메신저 피싱에서 자주 사용되는 대표적인 수법입니다. 송금을 하기 전에는 반드시 기존에 알고 있던 전화번호로 직접 연락하여 사실 여부를 확인하는 것이 중요합니다.',
        'case-return-delivery'
    ),
    (
        'case-return-delivery-voice-quiz-1',
        'case-return-delivery-voice',
        1,
        '다음 중 사기임을 판단할 수 있는 결정적인 단서를 모두 선택하세요.',
        '택배 기사가 방문 전에 연락하는 것은 일반적인 상황일 수 있습니다. 하지만 개인 번호를 이용해 개인정보를 다시 요구하거나, 문자 링크를 통한 주소 입력과 앱 설치를 요구하는 경우는 대표적인 택배 사칭 피싱 수법입니다.',
        NULL
    );
