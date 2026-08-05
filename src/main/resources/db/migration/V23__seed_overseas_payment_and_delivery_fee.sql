-- V23: 8·9번 시나리오를 추가하고 전화 가로채기 피싱의 카테고리를 보정한다.
--
-- 8. 해외결제 취소 사기
-- 9. 배송비 추가 결제 사기
--
-- 시나리오 원문: Notion '시나리오 형태 구성 (9번까지 고도화 완료)'

-- ---------------------------------------------------------------------------
-- 1. 카테고리 및 시나리오
-- ---------------------------------------------------------------------------
ALTER TABLE categories DROP CONSTRAINT chk_categories_name;
ALTER TABLE categories
    ADD CONSTRAINT chk_categories_name CHECK (
        category_name IN ('기관 사칭', '가족 사칭', '택배 사칭', '메신저 피싱', '투자 사기', '복합형')
    );

INSERT INTO categories (category_id, category_name)
VALUES ('category-composite-fraud', '복합형')
ON CONFLICT (category_id) DO NOTHING;

-- 전화 가로채기 피싱은 기관을 사칭하는 동시에 통화를 가로채는 복합형이다.
UPDATE case_scenarios
SET category_id = 'category-composite-fraud'
WHERE scenario_id = 'case-call-interception';

INSERT INTO case_scenarios (
    scenario_id,
    category_id,
    case_name,
    difficulty,
    estimated_learning_time,
    average_damage_amount,
    report_count
)
VALUES
    ('case-overseas-payment-cancellation', 'category-investment-fraud', '해외결제 취소 사기', '보통', '3분',
     '수천만 원 규모', '약 1,673건 (2023년 기준)'),
    ('case-delivery-fee-payment', 'category-delivery-impersonation', '배송비 추가 결제 사기', '보통', '3분',
     '수백만 원 규모', '약 31,000건 (최근 5년 평균)');

INSERT INTO case_variants (variant_id, scenario_id, channel)
VALUES
    ('case-overseas-payment-cancellation-voice', 'case-overseas-payment-cancellation', 'VOICE'),
    ('case-overseas-payment-cancellation-message', 'case-overseas-payment-cancellation', 'MESSAGE'),
    ('case-delivery-fee-payment-voice', 'case-delivery-fee-payment', 'VOICE'),
    ('case-delivery-fee-payment-message', 'case-delivery-fee-payment', 'MESSAGE');

-- ---------------------------------------------------------------------------
-- 2. 시나리오 본문
-- ---------------------------------------------------------------------------
UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '○○카드 보안센터입니다.' || CHR(10) ||
              '고객님 명의 카드로' || CHR(10) ||
              '599,000원 해외결제가 승인되었습니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"저는 결제한 적이 없는데요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"걱정하지 마십시오.' || CHR(10) ||
              '지금 바로 취소해드릴 수 있습니다.' || CHR(10) ||
              '다만 본인 확인과 결제 취소를 위해' || CHR(10) ||
              '보안 프로그램을 설치하셔야 합니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"앱을 꼭 설치해야 하나요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"설치하지 않으면' || CHR(10) ||
              '결제가 최종 승인될 수 있습니다.' || CHR(10) ||
              '지금 문자로 보내드리는 링크를 눌러' || CHR(10) ||
              '안내에 따라 진행해 주세요."'
WHERE variant_id = 'case-overseas-payment-cancellation-voice';

UPDATE case_variants
SET content = '[메신저]' || CHR(10) ||
              '○○카드 보안센터' || CHR(10) || CHR(10) ||
              '안녕하세요.' || CHR(10) || CHR(10) ||
              '고객님 명의 카드로' || CHR(10) ||
              '599,000원 해외결제가' || CHR(10) ||
              '승인되었습니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '○○카드 보안센터' || CHR(10) || CHR(10) ||
              '결제를 취소하려면' || CHR(10) ||
              '보안 프로그램 설치가 필요합니다.' || CHR(10) ||
              '아래 링크를 눌러 앱을 설치해 주세요.' || CHR(10) ||
              'https://xxxxx.kr' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '○○카드 보안센터' || CHR(10) || CHR(10) ||
              '설치가 완료되었습니다.' || CHR(10) ||
              '이제 본인 확인을 위해 카드번호와' || CHR(10) ||
              '문자로 받은 인증번호를 입력해 주세요.'
WHERE variant_id = 'case-overseas-payment-cancellation-message';

UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '○○택배 기사입니다.' || CHR(10) ||
              '고객님의 택배가 배송 중인데' || CHR(10) ||
              '추가 배송비가 발생했습니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"추가 배송비요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"배송지 확인 과정에서' || CHR(10) ||
              '3,000원의 추가 배송비가 발생했습니다.' || CHR(10) ||
              '결제가 완료되어야 오늘 정상 배송이 가능합니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"어떻게 결제하면 되나요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"문자로 보내드리는 링크에서' || CHR(10) ||
              '카드 정보를 입력하시면' || CHR(10) ||
              '바로 결제가 완료됩니다."'
WHERE variant_id = 'case-delivery-fee-payment-voice';

UPDATE case_variants
SET content = '[메신저]' || CHR(10) ||
              '○○택배' || CHR(10) || CHR(10) ||
              '안녕하세요.' || CHR(10) ||
              '고객님의 택배가 배송 중입니다.' || CHR(10) ||
              '배송지 확인 과정에서 추가 배송비 3,000원이 발생했습니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '오늘 안에 결제가 완료되지 않으면' || CHR(10) ||
              '배송이 자동 취소될 수 있습니다.' || CHR(10) ||
              '아래 링크에서 결제를 진행해 주세요.' || CHR(10) ||
              'https://xxxxx.kr' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '결제를 진행하려면 카드번호와 유효기간을 입력해 주세요.' || CHR(10) ||
              '본인 확인을 위해 문자로 받은 인증번호도 입력해 주셔야 합니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '결제가 완료되었습니다.' || CHR(10) ||
              '배송을 위해 주소를 다시 한번 입력해 주세요.'
WHERE variant_id = 'case-delivery-fee-payment-message';

-- ---------------------------------------------------------------------------
-- 3. 시뮬레이션 선택지
-- ---------------------------------------------------------------------------
INSERT INTO case_variant_options
    (option_id, variant_id, option_kind, step_number, option_number, option_text, is_correct)
VALUES
    ('case-overseas-payment-cancellation-voice-action-1-1', 'case-overseas-payment-cancellation-voice', 'ACTION', 1, 1, '문자로 받은 링크를 눌러 보안 프로그램을 설치한다.', FALSE),
    ('case-overseas-payment-cancellation-voice-action-1-2', 'case-overseas-payment-cancellation-voice', 'ACTION', 1, 2, '전화를 끊고 카드사 공식 고객센터에 직접 연락해 결제 내역을 확인한다.', TRUE),
    ('case-overseas-payment-cancellation-voice-action-1-3', 'case-overseas-payment-cancellation-voice', 'ACTION', 1, 3, '카드번호와 인증번호를 알려준다.', FALSE),
    ('case-overseas-payment-cancellation-voice-action-1-4', 'case-overseas-payment-cancellation-voice', 'ACTION', 1, 4, '결제가 취소될 것이라 믿고 안내를 계속 따른다.', FALSE),
    ('case-overseas-payment-cancellation-message-action-1-1', 'case-overseas-payment-cancellation-message', 'ACTION', 1, 1, '카드사 공식 고객센터를 통해 먼저 확인하겠습니다.', TRUE),
    ('case-overseas-payment-cancellation-message-action-1-2', 'case-overseas-payment-cancellation-message', 'ACTION', 1, 2, '빨리 취소해주세요.', FALSE),
    ('case-overseas-payment-cancellation-message-action-2-1', 'case-overseas-payment-cancellation-message', 'ACTION', 2, 1, '링크를 누르지 않고 카드사 공식 고객센터에 직접 문의한다.', TRUE),
    ('case-overseas-payment-cancellation-message-action-2-2', 'case-overseas-payment-cancellation-message', 'ACTION', 2, 2, '결제를 취소하기 위해 앱을 설치한다.', FALSE),
    ('case-overseas-payment-cancellation-message-action-3-1', 'case-overseas-payment-cancellation-message', 'ACTION', 3, 1, '카드번호와 인증번호를 입력한다.', FALSE),
    ('case-overseas-payment-cancellation-message-action-3-2', 'case-overseas-payment-cancellation-message', 'ACTION', 3, 2, '이상함을 느끼고 앱을 종료한 뒤 카드사에 직접 확인한다.', TRUE),

    ('case-delivery-fee-payment-voice-action-1-1', 'case-delivery-fee-payment-voice', 'ACTION', 1, 1, '문자로 받은 링크에서 카드 정보를 입력한다.', FALSE),
    ('case-delivery-fee-payment-voice-action-1-2', 'case-delivery-fee-payment-voice', 'ACTION', 1, 2, '택배사 공식 앱이나 고객센터를 통해 배송 상태를 직접 확인한다.', TRUE),
    ('case-delivery-fee-payment-voice-action-1-3', 'case-delivery-fee-payment-voice', 'ACTION', 1, 3, '배송이 늦어질까 봐 바로 결제한다.', FALSE),
    ('case-delivery-fee-payment-voice-action-1-4', 'case-delivery-fee-payment-voice', 'ACTION', 1, 4, '카드번호만 알려주면 된다고 생각하고 통화를 이어간다.', FALSE),
    ('case-delivery-fee-payment-message-action-1-1', 'case-delivery-fee-payment-message', 'ACTION', 1, 1, '택배사 공식 앱에서 배송 상태를 먼저 확인하겠습니다.', TRUE),
    ('case-delivery-fee-payment-message-action-1-2', 'case-delivery-fee-payment-message', 'ACTION', 1, 2, '알겠습니다. 바로 결제하겠습니다.', FALSE),
    ('case-delivery-fee-payment-message-action-2-1', 'case-delivery-fee-payment-message', 'ACTION', 2, 1, '링크를 닫고 택배사 공식 앱 또는 고객센터를 통해 직접 확인한다.', TRUE),
    ('case-delivery-fee-payment-message-action-2-2', 'case-delivery-fee-payment-message', 'ACTION', 2, 2, '카드번호와 인증번호를 입력한다.', FALSE),
    ('case-delivery-fee-payment-message-action-3-1', 'case-delivery-fee-payment-message', 'ACTION', 3, 1, '주소와 개인정보를 계속 입력한다.', FALSE),
    ('case-delivery-fee-payment-message-action-3-2', 'case-delivery-fee-payment-message', 'ACTION', 3, 2, '이상함을 느끼고 입력을 중단한 뒤 택배사에 직접 문의한다.', TRUE);

-- ---------------------------------------------------------------------------
-- 4. 마무리 퀴즈
-- ---------------------------------------------------------------------------
INSERT INTO case_variant_options
    (option_id, variant_id, option_kind, step_number, option_number, option_text, is_correct)
VALUES
    ('case-overseas-payment-cancellation-voice-option-1', 'case-overseas-payment-cancellation-voice', 'QUIZ', 1, 1, '해외결제가 승인되었다고 말했다.', FALSE),
    ('case-overseas-payment-cancellation-voice-option-2', 'case-overseas-payment-cancellation-voice', 'QUIZ', 1, 2, '카드사 직원이라고 소개했다.', FALSE),
    ('case-overseas-payment-cancellation-voice-option-3', 'case-overseas-payment-cancellation-voice', 'QUIZ', 1, 3, '결제 취소를 위해 원격제어 앱 설치를 요구했다.', TRUE),
    ('case-overseas-payment-cancellation-voice-option-4', 'case-overseas-payment-cancellation-voice', 'QUIZ', 1, 4, '결제를 바로 취소해주겠다고 말했다.', FALSE),
    ('case-overseas-payment-cancellation-message-option-1', 'case-overseas-payment-cancellation-message', 'QUIZ', 1, 1, '해외결제가 승인되었다는 안내를 받았을 때', FALSE),
    ('case-overseas-payment-cancellation-message-option-2', 'case-overseas-payment-cancellation-message', 'QUIZ', 1, 2, '결제를 취소해주겠다고 했을 때', FALSE),
    ('case-overseas-payment-cancellation-message-option-3', 'case-overseas-payment-cancellation-message', 'QUIZ', 1, 3, '결제 취소를 위해 앱 설치를 요구했을 때', TRUE),
    ('case-overseas-payment-cancellation-message-option-4', 'case-overseas-payment-cancellation-message', 'QUIZ', 1, 4, '카드번호를 확인하겠다고 했을 때', FALSE),
    ('case-delivery-fee-payment-voice-option-1', 'case-delivery-fee-payment-voice', 'QUIZ', 1, 1, '택배 기사라고 소개했다.', FALSE),
    ('case-delivery-fee-payment-voice-option-2', 'case-delivery-fee-payment-voice', 'QUIZ', 1, 2, '배송이 지연될 수 있다고 말했다.', FALSE),
    ('case-delivery-fee-payment-voice-option-3', 'case-delivery-fee-payment-voice', 'QUIZ', 1, 3, '문자로 보낸 링크에서 카드 정보를 입력하라고 했다.', TRUE),
    ('case-delivery-fee-payment-voice-option-4', 'case-delivery-fee-payment-voice', 'QUIZ', 1, 4, '추가 배송비가 발생했다고 말했다.', FALSE),
    ('case-delivery-fee-payment-message-option-1', 'case-delivery-fee-payment-message', 'QUIZ', 1, 1, '배송이 지연될 수 있다고 안내했을 때', FALSE),
    ('case-delivery-fee-payment-message-option-2', 'case-delivery-fee-payment-message', 'QUIZ', 1, 2, '추가 배송비가 발생했다고 했을 때', FALSE),
    ('case-delivery-fee-payment-message-option-3', 'case-delivery-fee-payment-message', 'QUIZ', 1, 3, '링크를 통해 카드번호와 인증번호 입력을 요구했을 때', TRUE),
    ('case-delivery-fee-payment-message-option-4', 'case-delivery-fee-payment-message', 'QUIZ', 1, 4, '오늘 안에 결제해야 한다고 했을 때', FALSE);

INSERT INTO case_variant_quizzes (
    quiz_id, variant_id, quiz_number, question, explanation, recommended_scenario_id
)
VALUES
    ('case-overseas-payment-cancellation-voice-quiz-1', 'case-overseas-payment-cancellation-voice', 1,
     '다음 중 금융사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
     '카드사는 결제 취소를 위해 원격제어 앱이나 보안 프로그램 설치를 요구하지 않습니다. 앱을 설치하거나 개인정보를 제공하지 말고 카드사 공식 고객센터를 통해 직접 결제 내역을 확인해야 합니다.',
     NULL),
    ('case-overseas-payment-cancellation-message-quiz-1', 'case-overseas-payment-cancellation-message', 1,
     '다음 중 금융사기를 가장 먼저 의심해야 하는 순간은 언제일까요?',
     '정상적인 카드사는 결제 취소를 이유로 원격제어 앱이나 보안 프로그램 설치를 요구하지 않습니다. 해외결제 승인 문자를 받았다면 메신저의 안내를 따르지 말고 카드사 공식 고객센터나 공식 앱을 통해 직접 결제 내역을 확인해야 합니다.',
     NULL),
    ('case-delivery-fee-payment-voice-quiz-1', 'case-delivery-fee-payment-voice', 1,
     '다음 중 택배 사칭 피싱임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
     '정상적인 택배사는 문자 링크를 통해 카드정보 입력을 요구하지 않습니다. 링크를 누르지 말고 택배사 공식 앱이나 고객센터를 통해 배송 상태를 직접 확인해야 합니다.',
     NULL),
    ('case-delivery-fee-payment-message-quiz-1', 'case-delivery-fee-payment-message', 1,
     '다음 중 택배 사칭 피싱임을 가장 먼저 의심해야 하는 순간은 언제일까요?',
     '정상적인 택배사는 문자 링크를 통해 카드번호와 인증번호 입력을 요구하지 않습니다. 개인정보 입력을 중단하고 택배사 공식 앱이나 고객센터를 통해 직접 확인해야 합니다.',
     NULL);

-- ---------------------------------------------------------------------------
-- 5. 취약 유형 태그
-- ---------------------------------------------------------------------------
INSERT INTO case_option_vulnerability_tags (option_id, vulnerability_type)
SELECT o.option_id, q.vulnerability_type
FROM (VALUES
    ('case-overseas-payment-cancellation-voice', 'ACTION', 1, 'AUTHORITY'),
    ('case-overseas-payment-cancellation-voice', 'ACTION', 1, 'FEAR'),
    ('case-overseas-payment-cancellation-voice', 'ACTION', 1, 'NO_VERIFY'),
    ('case-overseas-payment-cancellation-voice', 'QUIZ', 1, 'NO_VERIFY'),
    ('case-overseas-payment-cancellation-message', 'ACTION', 1, 'AUTHORITY'),
    ('case-overseas-payment-cancellation-message', 'ACTION', 1, 'NO_VERIFY'),
    ('case-overseas-payment-cancellation-message', 'ACTION', 2, 'NO_VERIFY'),
    ('case-overseas-payment-cancellation-message', 'ACTION', 3, 'NO_VERIFY'),
    ('case-overseas-payment-cancellation-message', 'QUIZ', 1, 'NO_VERIFY'),
    ('case-delivery-fee-payment-voice', 'ACTION', 1, 'AUTHORITY'),
    ('case-delivery-fee-payment-voice', 'ACTION', 1, 'NO_VERIFY'),
    ('case-delivery-fee-payment-voice', 'ACTION', 1, 'URGENCY'),
    ('case-delivery-fee-payment-voice', 'QUIZ', 1, 'NO_VERIFY'),
    ('case-delivery-fee-payment-message', 'ACTION', 1, 'AUTHORITY'),
    ('case-delivery-fee-payment-message', 'ACTION', 1, 'FEAR'),
    ('case-delivery-fee-payment-message', 'ACTION', 2, 'NO_VERIFY'),
    ('case-delivery-fee-payment-message', 'ACTION', 3, 'NO_VERIFY'),
    ('case-delivery-fee-payment-message', 'QUIZ', 1, 'NO_VERIFY')
) AS q(variant_id, option_kind, step_number, vulnerability_type)
JOIN case_variant_options o
     ON o.variant_id = q.variant_id
    AND o.option_kind = q.option_kind
    AND o.step_number = q.step_number
    AND o.is_correct = TRUE
ON CONFLICT (option_id, vulnerability_type) DO NOTHING;
