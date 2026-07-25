-- V18: 정본 '🎮 선택지'(행동 선택지)를 시드한다.
--
-- 배경: case_variant_options 에는 정본 '🧩 단서 찾기 퀴즈'의 보기만 들어가 있었고,
--       시뮬레이션 중 사용자가 고르는 '🎮 선택지'는 한 번도 저장된 적이 없었다.
--       명세(05. API 명세서_스크립트_선택지_추가 A79/API-078)의 응답 필드가 이 테이블의
--       컬럼과 1:1 대응하고, 논리 모델(04. 모델링 C54)이 선택지를 "행동 또는 답안 후보"로
--       규정하므로 신규 테이블을 만들지 않고 같은 테이블에 구분자를 두어 담는다.
--
-- 근거: .agent-team/02-market-analysis/gap-analysis.md G17

-- ---------------------------------------------------------------------------
-- 1. 구분 컬럼 추가
--    option_kind : QUIZ(단서 찾기 퀴즈의 보기) / ACTION(시뮬레이션 행동 선택지)
--    step_number : 대화 중 분기 회차. 정본 message 3건은 분기가 2회다.
--    기존 행은 전부 퀴즈 보기이므로 QUIZ / 1 로 채워진다.
-- ---------------------------------------------------------------------------
ALTER TABLE case_variant_options ADD COLUMN option_kind VARCHAR(20) DEFAULT 'QUIZ' NOT NULL;
ALTER TABLE case_variant_options ADD COLUMN step_number INTEGER DEFAULT 1 NOT NULL;

ALTER TABLE case_variant_options
    ADD CONSTRAINT chk_case_variant_options_kind
        CHECK (option_kind IN ('QUIZ', 'ACTION'));

-- 기존 UNIQUE(variant_id, option_number) 는 종류·회차를 구분하지 못해
-- 행동 선택지가 같은 option_number 를 쓸 수 없다. 범위를 넓힌다.
ALTER TABLE case_variant_options DROP CONSTRAINT uk_case_variant_options_variant_number;
ALTER TABLE case_variant_options
    ADD CONSTRAINT uk_case_variant_options_variant_kind_step_number
        UNIQUE (variant_id, option_kind, step_number, option_number);

-- ---------------------------------------------------------------------------
-- 2. 행동 선택지 시드 — 정본 '🎮 선택지' 전사
--    NOTE: 정본에 정답이 2개인 경우가 있다(휴대폰고장/택배주소오류사칭 VOICE).
--          is_correct 가 행 단위이므로 그대로 표현된다.
-- ---------------------------------------------------------------------------
INSERT INTO case_variant_options
    (option_id, variant_id, option_kind, step_number, option_number, option_text, is_correct)
VALUES
    -- 휴대폰고장 / VOICE — 정답 2개
    ('case-mobile-repair-voice-action-1-1', 'case-mobile-repair-voice', 'ACTION', 1, 1, '계좌번호를 받아 바로 송금한다.', FALSE),
    ('case-mobile-repair-voice-action-1-2', 'case-mobile-repair-voice', 'ACTION', 1, 2, '기존에 저장된 아들 번호로 직접 전화한다.', TRUE),
    ('case-mobile-repair-voice-action-1-3', 'case-mobile-repair-voice', 'ACTION', 1, 3, '상대방이 알려준 계좌로 송금한다.', FALSE),
    ('case-mobile-repair-voice-action-1-4', 'case-mobile-repair-voice', 'ACTION', 1, 4, '전화를 끊고 다른 가족에게 먼저 확인한다.', TRUE),

    -- 휴대폰고장 / MESSAGE — 2회 분기
    ('case-mobile-repair-message-action-1-1', 'case-mobile-repair-message', 'ACTION', 1, 1, '계좌번호를 받아 바로 송금한다.', FALSE),
    ('case-mobile-repair-message-action-1-2', 'case-mobile-repair-message', 'ACTION', 1, 2, '기존에 저장된 아들 번호로 직접 전화한다.', TRUE),
    ('case-mobile-repair-message-action-2-1', 'case-mobile-repair-message', 'ACTION', 2, 1, '송금을 진행한다.', FALSE),
    ('case-mobile-repair-message-action-2-2', 'case-mobile-repair-message', 'ACTION', 2, 2, '통화를 종료하고 가족에게 직접 확인한다.', TRUE),

    -- 택배주소오류사칭 / VOICE — 정답 2개
    ('case-return-delivery-voice-action-1-1', 'case-return-delivery-voice', 'ACTION', 1, 1, '문자 링크를 눌러 주소를 입력한다.', FALSE),
    ('case-return-delivery-voice-action-1-2', 'case-return-delivery-voice', 'ACTION', 1, 2, '공식 택배사 고객센터에 먼저 확인한다.', TRUE),
    ('case-return-delivery-voice-action-1-3', 'case-return-delivery-voice', 'ACTION', 1, 3, '링크에서 앱을 설치한다.', FALSE),
    ('case-return-delivery-voice-action-1-4', 'case-return-delivery-voice', 'ACTION', 1, 4, '전화를 종료하고 문자 링크를 삭제한다.', TRUE),

    -- 택배주소오류사칭 / MESSAGE — 2회 분기
    ('case-return-delivery-message-action-1-1', 'case-return-delivery-message', 'ACTION', 1, 1, '링크를 눌러 주소를 입력한다.', FALSE),
    ('case-return-delivery-message-action-1-2', 'case-return-delivery-message', 'ACTION', 1, 2, '반품 신청한 쇼핑몰 주문내역을 먼저 확인한다.', TRUE),
    ('case-return-delivery-message-action-2-1', 'case-return-delivery-message', 'ACTION', 2, 1, '개인정보를 입력한다.', FALSE),
    ('case-return-delivery-message-action-2-2', 'case-return-delivery-message', 'ACTION', 2, 2, '창을 닫고 택배사 공식 앱 또는 고객센터를 확인한다.', TRUE),

    -- 소방기관사기 / VOICE
    ('case-fire-agency-voice-action-1-1', 'case-fire-agency-voice', 'ACTION', 1, 1, '안내받은 업체에 바로 연락해 구매한다.', FALSE),
    ('case-fire-agency-voice-action-1-2', 'case-fire-agency-voice', 'ACTION', 1, 2, '관할 시청 또는 소방서 공식 대표번호로 직접 확인한다.', TRUE),
    ('case-fire-agency-voice-action-1-3', 'case-fire-agency-voice', 'ACTION', 1, 3, '문자로 받은 업체 계좌로 결제한다.', FALSE),
    ('case-fire-agency-voice-action-1-4', 'case-fire-agency-voice', 'ACTION', 1, 4, '과태료가 걱정되니 우선 구매부터 진행한다.', FALSE),

    -- 소방기관사기 / MESSAGE — 2회 분기
    ('case-fire-agency-message-action-1-1', 'case-fire-agency-message', 'ACTION', 1, 1, '안내받은 업체에 바로 연락한다.', FALSE),
    ('case-fire-agency-message-action-1-2', 'case-fire-agency-message', 'ACTION', 1, 2, '문자에 적힌 번호 대신 관할 소방서(또는 시청)에 직접 확인한다.', TRUE),
    ('case-fire-agency-message-action-2-1', 'case-fire-agency-message', 'ACTION', 2, 1, '업체에 결제를 진행한다.', FALSE),
    ('case-fire-agency-message-action-2-2', 'case-fire-agency-message', 'ACTION', 2, 2, '관할 소방서에 직접 문의하여 사실 여부를 확인한다.', TRUE),

    -- 특별투자상품권유 / VOICE
    ('case-special-investment-voice-action-1-1', 'case-special-investment-voice', 'ACTION', 1, 1, '안내받은 계좌로 바로 투자금을 송금한다.', FALSE),
    ('case-special-investment-voice-action-1-2', 'case-special-investment-voice', 'ACTION', 1, 2, '해당 금융회사가 실제 등록된 업체인지 공식 홈페이지에서 확인한다.', TRUE),
    ('case-special-investment-voice-action-1-3', 'case-special-investment-voice', 'ACTION', 1, 3, '수익이 궁금해 개인정보를 알려준다.', FALSE),
    ('case-special-investment-voice-action-1-4', 'case-special-investment-voice', 'ACTION', 1, 4, '기회를 놓칠까 봐 우선 소액만 투자해 본다.', FALSE),

    -- 특별투자상품권유 / MESSAGE
    ('case-special-investment-message-action-1-1', 'case-special-investment-message', 'ACTION', 1, 1, '문자에 있는 링크를 눌러 가입을 진행한다.', FALSE),
    ('case-special-investment-message-action-1-2', 'case-special-investment-message', 'ACTION', 1, 2, '문자에 있는 번호로 전화해 자세한 설명을 듣는다.', FALSE),
    ('case-special-investment-message-action-1-3', 'case-special-investment-message', 'ACTION', 1, 3, '링크를 누르지 않고 해당 금융회사가 실제 등록된 업체인지 공식 경로로 확인한다.', TRUE),
    ('case-special-investment-message-action-1-4', 'case-special-investment-message', 'ACTION', 1, 4, '기회를 놓칠까 봐 가족에게도 함께 투자하자고 권유한다.', FALSE),

    -- 새 번호로 온 가족의 송금 요청 / VOICE
    ('case-new-number-family-transfer-voice-action-1-1', 'case-new-number-family-transfer-voice', 'ACTION', 1, 1, '계좌번호를 받아 바로 송금한다.', FALSE),
    ('case-new-number-family-transfer-voice-action-1-2', 'case-new-number-family-transfer-voice', 'ACTION', 1, 2, '기존에 저장된 가족의 연락처로 직접 전화해 사실 여부를 확인한다.', TRUE),
    ('case-new-number-family-transfer-voice-action-1-3', 'case-new-number-family-transfer-voice', 'ACTION', 1, 3, '계좌번호를 받아 소액만 먼저 송금한다.', FALSE),
    ('case-new-number-family-transfer-voice-action-1-4', 'case-new-number-family-transfer-voice', 'ACTION', 1, 4, '급한 상황이니 다른 가족에게도 송금을 부탁한다.', FALSE),

    -- 새 번호로 온 가족의 송금 요청 / MESSAGE
    ('case-new-number-family-transfer-message-action-1-1', 'case-new-number-family-transfer-message', 'ACTION', 1, 1, '계좌번호를 받아 바로 송금한다.', FALSE),
    ('case-new-number-family-transfer-message-action-1-2', 'case-new-number-family-transfer-message', 'ACTION', 1, 2, '메신저로만 계속 대화를 이어간다.', FALSE),
    ('case-new-number-family-transfer-message-action-1-3', 'case-new-number-family-transfer-message', 'ACTION', 1, 3, '기존에 저장된 자녀의 연락처로 직접 전화하거나 가족에게 사실 여부를 확인한다.', TRUE),
    ('case-new-number-family-transfer-message-action-1-4', 'case-new-number-family-transfer-message', 'ACTION', 1, 4, '급한 상황 같으니 다른 가족에게도 돈을 모아 보내자고 한다.', FALSE);
