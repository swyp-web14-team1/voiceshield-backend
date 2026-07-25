-- V13: 정본(swyp/시나리오/*.md) 기준으로 비어 있던 콘텐츠를 채운다.
-- 스키마 변경 없음 — INSERT/UPDATE 만.
-- 근거: .agent-team/02-market-analysis/gap-analysis.md (G01 G02 G04 G05 G18 G19)

-- ---------------------------------------------------------------------------
-- NOTE: '메신저 피싱' 카테고리와 case-new-number-family-transfer 연결은
--       이번 범위에서 제외한다(기획 결정 — 메신저 사기는 차기 범위).
--       해당 시나리오는 category_id 가 NULL 이라 카탈로그에 노출되지 않는다.
--       아래 콘텐츠 시드에는 포함해 두었으므로, 차기에 카테고리만 연결하면 바로 동작한다.
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- G01: 난이도 — 전 시나리오 NULL 이었음. 정본 프로퍼티(난이도) 기준.
-- ---------------------------------------------------------------------------
UPDATE case_scenarios SET difficulty = '보통'   WHERE scenario_id = 'case-mobile-repair';
UPDATE case_scenarios SET difficulty = '쉬움'   WHERE scenario_id = 'case-return-delivery';
UPDATE case_scenarios SET difficulty = '보통'   WHERE scenario_id = 'case-fire-agency';
UPDATE case_scenarios SET difficulty = '어려움' WHERE scenario_id = 'case-special-investment';
UPDATE case_scenarios SET difficulty = '어려움' WHERE scenario_id = 'case-new-number-family-transfer';

-- ---------------------------------------------------------------------------
-- G02: 예상 학습 시간 — 전 시나리오 NULL 이었음. 정본 프로퍼티(예상 학습 시간) 기준.
-- NOTE: case-mobile-repair 만 채널별로 값이 다르다(voice 2분 / message 3분).
--       현행 컬럼이 시나리오 단위라 두 값을 함께 담을 수 없어 범위 표기로 넣는다.
--       (범위 표기는 정본 case-return-delivery 가 이미 쓰는 형식이다.)
--       채널별 보존은 컬럼 위치 이동이 필요하므로 별도 과제로 남긴다.
-- ---------------------------------------------------------------------------
UPDATE case_scenarios SET estimated_learning_time = '2~3분' WHERE scenario_id = 'case-mobile-repair';
UPDATE case_scenarios SET estimated_learning_time = '2~3분' WHERE scenario_id = 'case-return-delivery';
UPDATE case_scenarios SET estimated_learning_time = '3분'   WHERE scenario_id = 'case-fire-agency';
UPDATE case_scenarios SET estimated_learning_time = '4분'   WHERE scenario_id = 'case-special-investment';
UPDATE case_scenarios SET estimated_learning_time = '3분'   WHERE scenario_id = 'case-new-number-family-transfer';

-- ---------------------------------------------------------------------------
-- G18: 대화 스크립트(content) 10건 중 7건 NULL — 정본 Scenario 섹션 전사.
-- ---------------------------------------------------------------------------

-- 휴대폰고장 / MESSAGE
UPDATE case_variants
SET content = '[카카오톡]' || CHR(10) ||
              '○○(아들)' || CHR(10) ||
              '엄마 나야.' || CHR(10) ||
              '휴대폰 액정이 깨져서' || CHR(10) ||
              '수리 맡기고 임시폰으로 연락해.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '○○(아들)' || CHR(10) ||
              '아까 길에서 넘어졌는데' || CHR(10) ||
              '병원에서 검사받고 있어...' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '○○(아들)' || CHR(10) ||
              '보험 처리 전에' || CHR(10) ||
              '병원비 20만 원만' || CHR(10) ||
              '먼저 보내줄 수 있어?' || CHR(10) ||
              '금방 갚을게...'
WHERE variant_id = 'case-mobile-repair-message';

-- 택배주소오류사칭 / VOICE
UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '택배 기사입니다.' || CHR(10) ||
              '오늘 반품 수거 예정인데' || CHR(10) ||
              '주소 정보가 일부 지워져 확인이 어렵습니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"네? 정상적으로 신청했는데요."' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"요즘 시스템 오류가 자주 발생합니다.' || CHR(10) ||
              '문자로 보내드리는 링크에서' || CHR(10) ||
              '주소만 다시 입력해 주세요."'
WHERE variant_id = 'case-return-delivery-voice';

-- 택배주소오류사칭 / MESSAGE
UPDATE case_variants
SET content = '[문자 수신]' || CHR(10) ||
              '대한택배' || CHR(10) ||
              '고객님의 반품 신청이 접수되었습니다.' || CHR(10) ||
              '반품 주소 확인이 필요합니다.' || CHR(10) ||
              '아래 링크에서 주소를 다시 입력해 주세요.' || CHR(10) ||
              'https://delivery-check.kr/xxxxx'
WHERE variant_id = 'case-return-delivery-message';

-- 소방기관사기 / VOICE
UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '00시청 예방안전과 주무관입니다.' || CHR(10) ||
              '최근 소방시설법이 개정되어' || CHR(10) ||
              '이번 달 말까지 안전점검을 실시할 예정입니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"네, 어떤 내용인가요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"리튬이온 소화기가 설치되어 있지 않으면' || CHR(10) ||
              '과태료와 영업정지 대상이 될 수 있습니다.' || CHR(10) ||
              '정부 지원을 받을 수 있는 지정 업체를' || CHR(10) ||
              '안내해 드릴 테니 오늘 안에 구매하시면 됩니다."'
WHERE variant_id = 'case-fire-agency-voice';

-- 소방기관사기 / MESSAGE
UPDATE case_variants
SET content = '[문자]' || CHR(10) ||
              '[00시청 예방안전과]' || CHR(10) ||
              '안녕하세요.' || CHR(10) ||
              '최근 소방시설법 개정으로' || CHR(10) ||
              '숙박업소 대상 안전점검이 실시됩니다.' || CHR(10) ||
              '관련 안내를 위해 연락드립니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '[문자]' || CHR(10) ||
              '담당 주무관입니다.' || CHR(10) ||
              '리튬이온 소화기 미비치 시' || CHR(10) ||
              '과태료 및 영업정지 대상이 될 수 있습니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '[문자]' || CHR(10) ||
              '정부 지원이 가능한' || CHR(10) ||
              '지정 업체를 안내드립니다.' || CHR(10) ||
              '아래 업체에서 구매 후' || CHR(10) ||
              '지원금을 신청하시면 됩니다.' || CHR(10) ||
              '010-XXXX-XXXX'
WHERE variant_id = 'case-fire-agency-message';

-- 특별투자상품권유 / VOICE
UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '○○자산운용 투자전문가입니다.' || CHR(10) ||
              '현재 VIP 고객만 참여 가능한' || CHR(10) ||
              '특별 투자 상품을 안내드리고 있습니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"갑자기 저한테 왜 연락을 주셨나요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"오늘 안에 가입하시면' || CHR(10) ||
              '원금 보장은 물론' || CHR(10) ||
              '월 20% 이상의 수익도 기대하실 수 있습니다.' || CHR(10) ||
              '기회가 얼마 남지 않았으니' || CHR(10) ||
              '안내드리는 계좌로 투자금을 보내주시면' || CHR(10) ||
              '바로 가입을 진행해 드리겠습니다."'
WHERE variant_id = 'case-special-investment-voice';

-- 특별투자상품권유 / MESSAGE
UPDATE case_variants
SET content = '[문자 메시지]' || CHR(10) ||
              '[○○자산운용]' || CHR(10) ||
              '축하드립니다.' || CHR(10) ||
              'VIP 고객 대상으로' || CHR(10) ||
              'AI 반도체 특별 투자 상품에' || CHR(10) ||
              '참여하실 수 있습니다.' || CHR(10) ||
              '원금 보장' || CHR(10) ||
              '월 20% 예상 수익' || CHR(10) ||
              '오늘까지만 가입 가능합니다.' || CHR(10) ||
              '투자 신청' || CHR(10) ||
              'http://vip-invest-event.com'
WHERE variant_id = 'case-special-investment-message';

-- ---------------------------------------------------------------------------
-- G19: case-fire-agency-voice / case-special-investment-voice 는
--      퀴즈 보기·퀴즈가 모두 0건이었다. 정본 '단서 찾기 퀴즈' 전사.
-- ---------------------------------------------------------------------------
INSERT INTO case_variant_options (option_id, variant_id, option_number, option_text, is_correct)
VALUES
    ('case-fire-agency-voice-option-1', 'case-fire-agency-voice', 1, '소방시설법이 개정되었다고 안내했다.', FALSE),
    ('case-fire-agency-voice-option-2', 'case-fire-agency-voice', 2, '이번 달 안전점검이 예정되어 있다고 말했다.', FALSE),
    ('case-fire-agency-voice-option-3', 'case-fire-agency-voice', 3, '특정 업체에서 장비를 구매하라고 안내했다.', TRUE),
    ('case-fire-agency-voice-option-4', 'case-fire-agency-voice', 4, '정부 지원금을 받을 수 있다고 설명했다.', FALSE),
    ('case-special-investment-voice-option-1', 'case-special-investment-voice', 1, 'VIP 고객만 참여할 수 있다고 말했다.', FALSE),
    ('case-special-investment-voice-option-2', 'case-special-investment-voice', 2, '오늘 안에 가입해야 한다고 말했다.', FALSE),
    ('case-special-investment-voice-option-3', 'case-special-investment-voice', 3, '원금을 보장하면서 높은 수익을 약속했다.', TRUE),
    ('case-special-investment-voice-option-4', 'case-special-investment-voice', 4, '투자 상품을 소개해 주었다.', FALSE);

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
        'case-fire-agency-voice-quiz-1',
        'case-fire-agency-voice',
        1,
        '다음 중 사기임을 판단할 수 있는 결정적인 단서는 무엇일까요?',
        '법령 개정이나 안전점검은 실제로 있을 수 있는 행정 절차입니다. 정부 지원사업 역시 실제 운영되는 경우가 있습니다. 하지만 공공기관이 특정 업체를 지정해 물품 구매를 권유하거나 결제를 요구하는 것은 매우 의심해야 할 신호입니다. 이러한 전화를 받았다면 통화 내용을 그대로 믿지 말고 관할 시청이나 소방서의 공식 대표번호로 직접 사실 여부를 확인해야 합니다.',
        NULL
    ),
    (
        'case-special-investment-voice-quiz-1',
        'case-special-investment-voice',
        1,
        '다음 중 투자사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        'VIP 대상 상품이나 기간 한정 이벤트는 실제 금융상품에서도 있을 수 있습니다. 하지만 원금을 보장하면서 높은 수익까지 약속하는 것은 대표적인 투자사기의 특징입니다. 투자를 권유받았다면 안내받은 내용만 믿지 말고 해당 금융회사가 정식 등록된 업체인지 공식 홈페이지나 금융당국을 통해 먼저 확인해야 합니다.',
        NULL
    );
