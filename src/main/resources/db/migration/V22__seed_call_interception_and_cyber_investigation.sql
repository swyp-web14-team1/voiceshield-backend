-- V22: 신규 시나리오 2건을 정본에서 시드한다.
--
--   1. 전화 가로채기 피싱 (case-call-interception)   — 정본 시나리오/전화가로채기피싱_{voice,message}.md
--   2. 사이버수사대 사칭   (case-cyber-investigation) — 정본 시나리오/사이버수사대사칭_{voicd,message}.md
--
-- 스키마 변경 없음 — INSERT 만. 기존 행은 한 줄도 건드리지 않는다.
--
-- 이 마이그레이션이 채우는 것(시나리오당 VOICE/MESSAGE 2개 변형)
--   case_scenarios / case_variants / case_variants.content
--   case_variant_options (ACTION = 정본 '💬 당신의 반응은?' · '🎮 선택지', QUIZ = 정본 '🧩 단서 찾기 퀴즈')
--   case_variant_quizzes (문제 + 해설)
--   case_option_vulnerability_tags (V21 규칙 그대로 — 아래 §5)
--
-- 설계 결정
--   (a) 카테고리는 둘 다 '기관 사칭'(category-institution-impersonation)이다.
--       전화 가로채기는 은행 보안센터를, 사이버수사대 사칭은 경찰청을 사칭한다.
--       '메신저 피싱' 카테고리는 V17 주석대로 여전히 미연결 상태로 둔다(기획 결정).
--
--   (b) average_damage_amount / report_count 는 넣지 않는다(NULL).
--       V19가 채운 5건은 정본에 통계 수치가 있었기 때문이고, 이번 정본 4건에는 없다.
--       없는 수치를 지어내지 않는다. 통계가 확보되면 별도 마이그레이션으로 채운다.
--
--   (c) 전화 가로채기는 분기가 3회다(기존 최대 2회). step_number 가 이미 회차를 담고
--       있어 스키마 변경 없이 들어간다. VOICE 는 보기 4개, MESSAGE 는 정본대로 보기 2개다.
--
--   (d) 태그는 V21 과 동일하게 option_id 를 나열하지 않고 문항 좌표로 선언한 뒤
--       is_correct = TRUE 인 행에 조인한다. 정답이 뒤집혀도 태그가 따라간다.

-- ---------------------------------------------------------------------------
-- 1. 시나리오 / 변형
-- ---------------------------------------------------------------------------
INSERT INTO case_scenarios (
    scenario_id,
    category_id,
    case_name,
    difficulty,
    estimated_learning_time
)
VALUES
    -- 피해자가 '직접 확인하려는 행동'까지 악용하는 복합형이라 난이도를 가장 높게 둔다.
    ('case-call-interception',   'category-institution-impersonation', '전화 가로채기 피싱', '어려움', '4분'),
    ('case-cyber-investigation', 'category-institution-impersonation', '사이버수사대 사칭',   '보통',   '3분');

INSERT INTO case_variants (variant_id, scenario_id, channel)
VALUES
    ('case-call-interception-voice',     'case-call-interception',   'VOICE'),
    ('case-call-interception-message',   'case-call-interception',   'MESSAGE'),
    ('case-cyber-investigation-voice',   'case-cyber-investigation', 'VOICE'),
    ('case-cyber-investigation-message', 'case-cyber-investigation', 'MESSAGE');

-- ---------------------------------------------------------------------------
-- 2. 대화 스크립트 — 정본 Scenario 섹션 전사
--    분기 이후 대화까지 한 content 에 담고 '----' 로 나눈다(V17 의 message 3건과 같은 형식).
-- ---------------------------------------------------------------------------

-- 전화 가로채기 피싱 / VOICE
UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '○○은행 보안센터입니다.' || CHR(10) ||
              '고객님 명의 계좌가' || CHR(10) ||
              '금융사기에 이용된 정황이 확인되었습니다."' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '나' || CHR(10) ||
              '"정말 은행 맞나요?' || CHR(10) ||
              '대표번호로 직접 확인해볼게요."' || CHR(10) ||
              '상대' || CHR(10) ||
              '"네.' || CHR(10) ||
              '대표번호로 확인하셔도 됩니다.' || CHR(10) ||
              '다만 사건이 진행 중이니' || CHR(10) ||
              '전화를 끊지 말고' || CHR(10) ||
              '그대로 대표번호를 눌러주세요."' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '(기존 통화는 종료되지 않음)' || CHR(10) ||
              '잠시 후' || CHR(10) ||
              '가짜 상담원' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '○○은행 고객센터입니다.' || CHR(10) ||
              '계좌 보호를 위해' || CHR(10) ||
              '본인 확인이 필요합니다."' || CHR(10) ||
              '"계좌번호와' || CHR(10) ||
              '문자로 받은 인증번호를' || CHR(10) ||
              '말씀해 주세요."'
WHERE variant_id = 'case-call-interception-voice';

-- 전화 가로채기 피싱 / MESSAGE
UPDATE case_variants
SET content = '[메신저]' || CHR(10) ||
              '○○은행 보안센터' || CHR(10) ||
              '안녕하세요.' || CHR(10) ||
              '고객님 명의 계좌가' || CHR(10) ||
              '금융사기에 이용된 정황이' || CHR(10) ||
              '확인되었습니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '○○은행 보안센터' || CHR(10) ||
              '의심되시면' || CHR(10) ||
              '은행 대표번호로' || CHR(10) ||
              '직접 확인하셔도 됩니다.' || CHR(10) ||
              '다만 사건이 진행 중이니' || CHR(10) ||
              '현재 통화는 끊지 마시고' || CHR(10) ||
              '그대로 대표번호를 눌러 확인해 주세요.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '○○은행 고객센터' || CHR(10) ||
              '안녕하세요.' || CHR(10) ||
              '○○은행 고객센터입니다.' || CHR(10) ||
              '계좌 보호를 위해' || CHR(10) ||
              '본인 확인이 필요합니다.' || CHR(10) ||
              '계좌번호와' || CHR(10) ||
              '문자로 받은 인증번호를' || CHR(10) ||
              '입력해 주세요.'
WHERE variant_id = 'case-call-interception-message';

-- 사이버수사대 사칭 / VOICE
UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"안녕하세요.' || CHR(10) ||
              '서울경찰청 사이버수사대입니다.' || CHR(10) ||
              '현재 고객님 명의 계좌가' || CHR(10) ||
              '보이스피싱 사건에 사용된 것으로 확인되었습니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"무슨 말씀이신가요?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"본인 확인이 필요합니다.' || CHR(10) ||
              '주민등록번호와 사용 중인 계좌번호를' || CHR(10) ||
              '확인해 주시면 사건 여부를 조회해드리겠습니다."' || CHR(10) ||
              '나' || CHR(10) ||
              '"정말 경찰에서 전화하신 건가요?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"사건이 진행 중이라 시간이 없습니다.' || CHR(10) ||
              '지금 바로 확인하지 않으면' || CHR(10) ||
              '불이익을 받을 수 있습니다."'
WHERE variant_id = 'case-cyber-investigation-voice';

-- 사이버수사대 사칭 / MESSAGE
UPDATE case_variants
SET content = '[메신저]' || CHR(10) ||
              '서울경찰청 사이버수사대' || CHR(10) ||
              '안녕하세요.' || CHR(10) ||
              '서울경찰청 사이버수사대입니다.' || CHR(10) ||
              '고객님 명의 계좌가' || CHR(10) ||
              '보이스피싱 사건에 연루된 것으로' || CHR(10) ||
              '확인되었습니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '서울경찰청 사이버수사대' || CHR(10) ||
              '사건 확인을 위해' || CHR(10) ||
              '주민등록번호와 계좌번호를' || CHR(10) ||
              '회신해주시기 바랍니다.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '서울경찰청 사이버수사대' || CHR(10) ||
              '사건 내용을 확인하시려면' || CHR(10) ||
              '아래 링크를 눌러' || CHR(10) ||
              '본인 인증을 진행해 주세요.' || CHR(10) ||
              'https://xxxxx.kr'
WHERE variant_id = 'case-cyber-investigation-message';

-- ---------------------------------------------------------------------------
-- 3. 행동 선택지(ACTION) — 정본 '💬 당신의 반응은?' / '🎮 선택지' 전사
--    정본의 ①②③④ 번호는 option_number 로 옮기고 문구에서는 뗀다(V14/V18 과 같은 형식).
-- ---------------------------------------------------------------------------
INSERT INTO case_variant_options
    (option_id, variant_id, option_kind, step_number, option_number, option_text, is_correct)
VALUES
    -- 전화 가로채기 / VOICE — 3회 분기
    --   1회차: 은행 사칭 + 계좌 범죄 연루 통보 직후
    ('case-call-interception-voice-action-1-1', 'case-call-interception-voice', 'ACTION', 1, 1, '무슨 일인지 먼저 자세히 들어본다.', FALSE),
    ('case-call-interception-voice-action-1-2', 'case-call-interception-voice', 'ACTION', 1, 2, '전화를 끊고 나중에 다시 확인한다.', FALSE),
    ('case-call-interception-voice-action-1-3', 'case-call-interception-voice', 'ACTION', 1, 3, '정말 은행인지 대표번호로 확인하겠다고 말한다.', TRUE),
    ('case-call-interception-voice-action-1-4', 'case-call-interception-voice', 'ACTION', 1, 4, '계좌가 정말 위험한지 바로 물어본다.', FALSE),
    --   2회차: "전화를 끊지 말고 그대로 대표번호를 눌러라" — 이 시나리오의 핵심 분기
    ('case-call-interception-voice-action-2-1', 'case-call-interception-voice', 'ACTION', 2, 1, '안내대로 전화를 끊지 않고 대표번호를 누른다.', FALSE),
    ('case-call-interception-voice-action-2-2', 'case-call-interception-voice', 'ACTION', 2, 2, '상대에게 직원번호를 알려달라고 한다.', FALSE),
    ('case-call-interception-voice-action-2-3', 'case-call-interception-voice', 'ACTION', 2, 3, '통화를 완전히 종료한 뒤 직접 다시 대표번호로 건다.', TRUE),
    ('case-call-interception-voice-action-2-4', 'case-call-interception-voice', 'ACTION', 2, 4, '계속 통화를 유지한 채 상담을 받는다.', FALSE),
    --   3회차: 가짜 상담원이 계좌번호·인증번호를 요구
    ('case-call-interception-voice-action-3-1', 'case-call-interception-voice', 'ACTION', 3, 1, '계좌번호와 인증번호를 알려준다.', FALSE),
    ('case-call-interception-voice-action-3-2', 'case-call-interception-voice', 'ACTION', 3, 2, '본인 확인이니까 알려준다.', FALSE),
    ('case-call-interception-voice-action-3-3', 'case-call-interception-voice', 'ACTION', 3, 3, '전화를 끊고 다른 휴대전화로 다시 대표번호를 확인한다.', TRUE),
    ('case-call-interception-voice-action-3-4', 'case-call-interception-voice', 'ACTION', 3, 4, '계좌 보호 절차를 계속 진행한다.', FALSE),

    -- 전화 가로채기 / MESSAGE — 3회 분기, 정본은 회차마다 보기 2개다
    ('case-call-interception-message-action-1-1', 'case-call-interception-message', 'ACTION', 1, 1, '정말 은행에서 보낸 메시지가 맞는지 되묻는다.', TRUE),
    ('case-call-interception-message-action-1-2', 'case-call-interception-message', 'ACTION', 1, 2, '무슨 일이니 빨리 해결해달라고 답한다.', FALSE),
    ('case-call-interception-message-action-2-1', 'case-call-interception-message', 'ACTION', 2, 1, '기존 통화를 완전히 종료한 뒤 대표번호로 다시 확인한다.', TRUE),
    ('case-call-interception-message-action-2-2', 'case-call-interception-message', 'ACTION', 2, 2, '대표번호를 직접 누르는 거니까 괜찮다고 생각하고 그대로 진행한다.', FALSE),
    ('case-call-interception-message-action-3-1', 'case-call-interception-message', 'ACTION', 3, 1, '인증번호를 입력한다.', FALSE),
    ('case-call-interception-message-action-3-2', 'case-call-interception-message', 'ACTION', 3, 2, '갑자기 이상함을 느끼고 통화를 종료한다.', TRUE),

    -- 사이버수사대 사칭 / VOICE — 1회 분기
    ('case-cyber-investigation-voice-action-1-1', 'case-cyber-investigation-voice', 'ACTION', 1, 1, '주민등록번호와 계좌번호를 알려준다.', FALSE),
    ('case-cyber-investigation-voice-action-1-2', 'case-cyber-investigation-voice', 'ACTION', 1, 2, '전화를 끊고 경찰청 공식 대표번호로 직접 확인한다.', TRUE),
    ('case-cyber-investigation-voice-action-1-3', 'case-cyber-investigation-voice', 'ACTION', 1, 3, '문자로 온 링크를 눌러 사건 내용을 확인한다.', FALSE),
    ('case-cyber-investigation-voice-action-1-4', 'case-cyber-investigation-voice', 'ACTION', 1, 4, '상대방의 안내에 따라 계속 통화를 이어간다.', FALSE),

    -- 사이버수사대 사칭 / MESSAGE — 1회 분기
    ('case-cyber-investigation-message-action-1-1', 'case-cyber-investigation-message', 'ACTION', 1, 1, '주민등록번호와 계좌번호를 전송한다.', FALSE),
    ('case-cyber-investigation-message-action-1-2', 'case-cyber-investigation-message', 'ACTION', 1, 2, '전달받은 링크를 눌러 본인 인증을 진행한다.', FALSE),
    ('case-cyber-investigation-message-action-1-3', 'case-cyber-investigation-message', 'ACTION', 1, 3, '경찰청 공식 대표번호로 직접 연락해 사실 여부를 확인한다.', TRUE),
    ('case-cyber-investigation-message-action-1-4', 'case-cyber-investigation-message', 'ACTION', 1, 4, '메신저로 계속 대화를 이어가며 안내를 따른다.', FALSE);

-- ---------------------------------------------------------------------------
-- 4. 단서 찾기 퀴즈 — 보기(QUIZ) + 문제/해설
-- ---------------------------------------------------------------------------
INSERT INTO case_variant_options
    (option_id, variant_id, option_kind, step_number, option_number, option_text, is_correct)
VALUES
    ('case-call-interception-voice-option-1', 'case-call-interception-voice', 'QUIZ', 1, 1, '은행 직원이라고 소개했다.', FALSE),
    ('case-call-interception-voice-option-2', 'case-call-interception-voice', 'QUIZ', 1, 2, '계좌가 범죄에 이용되었다고 말했다.', FALSE),
    ('case-call-interception-voice-option-3', 'case-call-interception-voice', 'QUIZ', 1, 3, '전화를 끊지 말고 대표번호로 확인하라고 안내했다.', TRUE),
    ('case-call-interception-voice-option-4', 'case-call-interception-voice', 'QUIZ', 1, 4, '사건이 급하다고 말했다.', FALSE),

    ('case-call-interception-message-option-1', 'case-call-interception-message', 'QUIZ', 1, 1, '계좌가 범죄에 이용됐다고 말했을 때', FALSE),
    ('case-call-interception-message-option-2', 'case-call-interception-message', 'QUIZ', 1, 2, '대표번호로 확인해도 된다고 말했을 때', FALSE),
    ('case-call-interception-message-option-3', 'case-call-interception-message', 'QUIZ', 1, 3, '전화를 끊지 말고 대표번호로 확인하라고 안내했을 때', TRUE),
    ('case-call-interception-message-option-4', 'case-call-interception-message', 'QUIZ', 1, 4, '상담원이 계좌 보호를 도와주겠다고 말했을 때', FALSE),

    ('case-cyber-investigation-voice-option-1', 'case-cyber-investigation-voice', 'QUIZ', 1, 1, '경찰이라고 자신을 소개했다.', FALSE),
    ('case-cyber-investigation-voice-option-2', 'case-cyber-investigation-voice', 'QUIZ', 1, 2, '전화로 개인정보와 계좌 정보를 요구했다.', TRUE),
    ('case-cyber-investigation-voice-option-3', 'case-cyber-investigation-voice', 'QUIZ', 1, 3, '사건이 진행 중이라고 말했다.', FALSE),
    ('case-cyber-investigation-voice-option-4', 'case-cyber-investigation-voice', 'QUIZ', 1, 4, '시간이 없다고 말했다.', FALSE),

    ('case-cyber-investigation-message-option-1', 'case-cyber-investigation-message', 'QUIZ', 1, 1, '경찰청이라고 소개했다.', FALSE),
    ('case-cyber-investigation-message-option-2', 'case-cyber-investigation-message', 'QUIZ', 1, 2, '사건에 연루되었다고 말했다.', FALSE),
    ('case-cyber-investigation-message-option-3', 'case-cyber-investigation-message', 'QUIZ', 1, 3, '메신저로 개인정보를 요구하고 링크를 통해 본인 인증을 유도했다.', TRUE),
    ('case-cyber-investigation-message-option-4', 'case-cyber-investigation-message', 'QUIZ', 1, 4, '사건을 빨리 확인해야 한다고 말했다.', FALSE);

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
        'case-call-interception-voice-quiz-1',
        'case-call-interception-voice',
        1,
        '다음 중 복합형 보이스피싱임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        '복합형 보이스피싱은 피해자가 직접 확인하려는 행동까지 악용하는 것이 핵심 특징입니다. 정상적인 은행이나 금융기관은 전화를 끊지 않은 상태에서 대표번호로 확인하도록 안내하지 않습니다. 사기범은 기존 통화를 유지한 채 대표번호를 누르게 만들어 통화를 가로채고, 이후 가짜 상담원으로 연결해 개인정보나 인증번호를 요구합니다. 의심스러운 전화를 받았다면 반드시 기존 통화를 완전히 종료한 뒤, 다른 휴대전화나 일정 시간이 지난 후 공식 대표번호로 다시 확인해야 합니다.',
        'case-cyber-investigation'
    ),
    (
        'case-call-interception-message-quiz-1',
        'case-call-interception-message',
        1,
        '다음 중 복합형 피싱임을 가장 먼저 의심해야 하는 순간은 언제일까요?',
        '복합형 피싱은 피해자가 직접 확인하려는 행동까지 악용하는 고도화된 수법입니다. 정상적인 은행이나 금융기관은 기존 통화를 유지한 채 대표번호로 확인하도록 안내하지 않습니다. 사기범은 전화를 끊지 않은 상태에서 대표번호를 누르게 만들어 통화를 가로채고, 이후 가짜 상담원으로 연결해 개인정보나 인증번호를 요구합니다. 의심스러운 연락을 받았다면 기존 통화를 반드시 완전히 종료한 뒤, 가능하면 다른 휴대전화나 일정 시간이 지난 후 공식 대표번호로 다시 확인하는 것이 안전합니다.',
        'case-cyber-investigation'
    ),
    (
        'case-cyber-investigation-voice-quiz-1',
        'case-cyber-investigation-voice',
        1,
        '다음 중 사기임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        '경찰이나 검찰은 전화로 주민등록번호, 계좌번호, OTP 등 금융정보를 요구하지 않습니다. 이와 같은 전화를 받았다면 상대방의 안내를 따르지 말고, 전화를 끊은 뒤 공식 대표번호나 가까운 경찰서를 통해 직접 사실 여부를 확인해야 합니다.',
        'case-call-interception'
    ),
    (
        'case-cyber-investigation-message-quiz-1',
        'case-cyber-investigation-message',
        1,
        '다음 중 메신저 피싱임을 판단할 수 있는 가장 결정적인 단서는 무엇일까요?',
        '경찰이나 검찰은 메신저로 주민등록번호, 계좌번호 등 개인정보를 요구하거나, 링크를 통해 본인 인증을 진행하도록 안내하지 않습니다. 이러한 메시지를 받았다면 링크를 누르거나 개인정보를 입력하지 말고, 공식 대표번호를 통해 직접 사실 여부를 확인해야 합니다.',
        'case-call-interception'
    );

-- ---------------------------------------------------------------------------
-- 5. 취약 유형 태그
--
--    규칙은 V21 과 동일하다. 근거는 정본의 '⚠️ 핵심 수법' · '🚨 취약 포인트' · '해설'
--    세 섹션이고, 각 문항의 태그는 "그 문항의 정답이 무력화하는 수법"으로 정했다.
--
--    NOTE: 이번 2건은 FEAR 를 크게 보강한다. 기존 정본에서 불이익으로 압박하는 사례는
--          소방기관사기 하나뿐이라 FEAR 문항이 3개였고, 최소 시도 3회 기준 때문에
--          한 채널만 학습하면 FEAR 가 영영 UNKNOWN 으로 남았다.
--          '계좌가 범죄에 연루됐다', '지금 확인하지 않으면 불이익' 이 둘 다 FEAR 라
--          이번 시드로 FEAR 문항이 3 → 7 개, 대상 시나리오가 1 → 3 개가 된다.
-- ---------------------------------------------------------------------------
INSERT INTO case_option_vulnerability_tags (option_id, vulnerability_type)
SELECT o.option_id, q.vulnerability_type
FROM (VALUES
    -- 전화 가로채기 피싱 — 은행 사칭 / 계좌 범죄 연루로 불안감 / 대표번호 확인을 안심시키며 유도 /
    --                      기존 통화를 끊지 않은 채 가로채기
    --   취약 포인트(voice): "계좌가 위험하다는 말에 당황함", "'대표번호로 확인해도 된다'는 말에 안심함",
    --                      "기존 통화를 끊지 않은 채 다시 전화를 걸도록 유도"
    --   1회차 정답: "정말 은행인지 대표번호로 확인하겠다고 말한다" — 사칭·불안·무검증을 한꺼번에 끊는다
    ('case-call-interception-voice',   'ACTION', 1, 'AUTHORITY'),
    ('case-call-interception-voice',   'ACTION', 1, 'FEAR'),
    ('case-call-interception-voice',   'ACTION', 1, 'NO_VERIFY'),
    --   2회차 정답: "통화를 완전히 종료한 뒤 직접 다시 대표번호로 건다"
    --              — '사건이 진행 중이니 끊지 말라'는 재촉(URGENCY)과 가짜 검증(NO_VERIFY)을 깬다
    ('case-call-interception-voice',   'ACTION', 2, 'NO_VERIFY'),
    ('case-call-interception-voice',   'ACTION', 2, 'URGENCY'),
    --   3회차 정답: "전화를 끊고 다른 휴대전화로 다시 대표번호를 확인한다" — 가짜 고객센터 사칭
    ('case-call-interception-voice',   'ACTION', 3, 'AUTHORITY'),
    ('case-call-interception-voice',   'ACTION', 3, 'NO_VERIFY'),
    --   퀴즈 정답: "전화를 끊지 말고 대표번호로 확인하라고 안내했다"
    ('case-call-interception-voice',   'QUIZ',   1, 'AUTHORITY'),
    ('case-call-interception-voice',   'QUIZ',   1, 'NO_VERIFY'),

    --   취약 포인트(message): "'지금 바로 확인해야 한다'며 긴급함을 강조",
    --                        "대표번호로 확인하면 안전하다고 안심시킴",
    --                        "피해자가 통화를 완전히 종료했다고 착각하게 만듦"
    ('case-call-interception-message', 'ACTION', 1, 'AUTHORITY'),
    ('case-call-interception-message', 'ACTION', 1, 'FEAR'),
    ('case-call-interception-message', 'ACTION', 1, 'NO_VERIFY'),
    ('case-call-interception-message', 'ACTION', 2, 'NO_VERIFY'),
    ('case-call-interception-message', 'ACTION', 2, 'URGENCY'),
    ('case-call-interception-message', 'ACTION', 3, 'AUTHORITY'),
    ('case-call-interception-message', 'ACTION', 3, 'NO_VERIFY'),
    ('case-call-interception-message', 'QUIZ',   1, 'AUTHORITY'),
    ('case-call-interception-message', 'QUIZ',   1, 'NO_VERIFY'),

    -- 사이버수사대 사칭 — 수사기관 사칭 / 범죄 연루 통보 / 개인정보·금융정보 요구 /
    --                     긴급한 상황을 만들어 판단을 서두르게 함
    --   취약 포인트(voice): "범죄에 연루됐다는 말에 불안감 유발", "체포·수사 등을 언급하며 심리적 압박",
    --                      "생각할 시간을 주지 않고 즉시 정보 제공 요구"
    --   이 시나리오는 분기가 1회뿐이라 그 한 문항이 네 수법을 모두 진다.
    ('case-cyber-investigation-voice',   'ACTION', 1, 'AUTHORITY'),
    ('case-cyber-investigation-voice',   'ACTION', 1, 'FEAR'),
    ('case-cyber-investigation-voice',   'ACTION', 1, 'URGENCY'),
    ('case-cyber-investigation-voice',   'ACTION', 1, 'NO_VERIFY'),
    --   퀴즈 정답: "전화로 개인정보와 계좌 정보를 요구했다" — 기관이라는 이유로 검증을 건너뛰는 지점
    ('case-cyber-investigation-voice',   'QUIZ',   1, 'AUTHORITY'),
    ('case-cyber-investigation-voice',   'QUIZ',   1, 'NO_VERIFY'),

    --   취약 포인트(message): "범죄에 연루됐다는 불안감 조성", "'즉시 확인'을 강조하며 판단을 서두르게 함",
    --                        "공식 기관인 것처럼 신뢰감을 형성"
    ('case-cyber-investigation-message', 'ACTION', 1, 'AUTHORITY'),
    ('case-cyber-investigation-message', 'ACTION', 1, 'FEAR'),
    ('case-cyber-investigation-message', 'ACTION', 1, 'URGENCY'),
    ('case-cyber-investigation-message', 'ACTION', 1, 'NO_VERIFY'),
    --   퀴즈 정답: "메신저로 개인정보를 요구하고 링크를 통해 본인 인증을 유도했다"
    ('case-cyber-investigation-message', 'QUIZ',   1, 'AUTHORITY'),
    ('case-cyber-investigation-message', 'QUIZ',   1, 'NO_VERIFY')
) AS q(variant_id, option_kind, step_number, vulnerability_type)
JOIN case_variant_options o
     ON  o.variant_id  = q.variant_id
     AND o.option_kind = q.option_kind
     AND o.step_number = q.step_number
     AND o.is_correct  = TRUE
ON CONFLICT (option_id, vulnerability_type) DO NOTHING;
