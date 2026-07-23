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
    ),
    (
        'case-return-delivery-message-quiz-1',
        'case-return-delivery-message',
        1,
        '다음 중 사기 문자의 결정적인 단서를 모두 선택하세요.',
        '반품 신청 이후 문자가 오는 것 자체는 정상적인 상황일 수 있습니다. 하지만 출처가 불분명한 링크를 보내거나 개인정보를 다시 입력하도록 유도하는 경우는 대표적인 택배 피싱 수법입니다.',
        NULL
    ),
    (
        'case-fire-agency-message-quiz-1',
        'case-fire-agency-message',
        1,
        '다음 중 사기임을 의심할 수 있는 결정적인 단서를 선택하세요.',
        '법령 개정, 안전점검, 정부 지원사업은 실제로 있을 수 있는 내용입니다. 하지만 공공기관이 특정 업체를 지정하여 구매를 유도하거나 결제를 요구하는 것은 매우 의심해야 할 신호입니다. 이런 안내를 받았다면 문자나 전화에 있는 번호를 이용하지 말고, 관할 시청이나 소방서의 공식 대표번호로 직접 확인해야 합니다.',
        NULL
    ),
    (
        'case-special-investment-message-quiz-1',
        'case-special-investment-message',
        1,
        '다음 중 투자사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        'AI 관련 투자 상품이나 VIP 대상 이벤트는 실제 금융상품에서도 있을 수 있습니다. 하지만 원금을 보장하면서 높은 수익을 동시에 약속하는 것은 대표적인 투자사기의 특징입니다. 또한 문자에 포함된 링크는 가짜 투자 사이트로 연결될 수 있으므로 절대 누르지 말고, 해당 금융회사가 실제 등록된 업체인지 공식 홈페이지나 금융당국을 통해 확인해야 합니다.',
        NULL
    ),
    (
        'case-new-number-family-transfer-voice-quiz-1',
        'case-new-number-family-transfer-voice',
        1,
        '다음 중 사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        '휴대폰 고장이나 급한 결제 상황은 실제로도 발생할 수 있습니다. 하지만 새 번호로 연락해 곧바로 송금을 요청하는 것은 가족 사칭 사기의 대표적인 특징입니다. 이럴 때는 절대로 바로 송금하지 말고, 기존에 저장된 연락처로 직접 전화하거나 다른 가족을 통해 사실 여부를 확인해야 합니다.',
        'case-mobile-repair'
    ),
    (
        'case-new-number-family-transfer-message-quiz-1',
        'case-new-number-family-transfer-message',
        1,
        '다음 중 메신저사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        '휴대폰 고장이나 급한 결제 상황은 실제로도 발생할 수 있습니다. 하지만 통화를 계속 피하면서 메신저로만 송금을 요구하는 것은 메신저사기의 대표적인 특징입니다. 이럴 때는 절대로 바로 송금하지 말고, 기존에 저장된 연락처로 직접 전화하거나 다른 가족을 통해 사실 여부를 확인해야 합니다.',
        'case-mobile-repair'
    );
