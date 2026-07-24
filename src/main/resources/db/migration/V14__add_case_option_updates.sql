INSERT INTO case_variant_options (option_id, variant_id, option_number, option_text, is_correct)
VALUES
    ('case-mobile-repair-message-option-1', 'case-mobile-repair-message', 1, '새로운 번호 또는 임시폰으로 연락했다.', FALSE),
    ('case-mobile-repair-message-option-2', 'case-mobile-repair-message', 2, '병원비를 개인 계좌로 바로 송금해 달라고 했다.', TRUE),
    ('case-mobile-repair-message-option-3', 'case-mobile-repair-message', 3, '기존 번호로는 연락하지 말라고 했다.', FALSE),
    ('case-mobile-repair-message-option-4', 'case-mobile-repair-message', 4, '다쳤다고 말했다.', FALSE),
    ('case-return-delivery-voice-option-1', 'case-return-delivery-voice', 1, '택배 기사가 방문 전 전화한다.', FALSE),
    ('case-return-delivery-voice-option-2', 'case-return-delivery-voice', 2, '개인 휴대전화 번호로 주소를 다시 요구한다.', TRUE),
    ('case-return-delivery-voice-option-3', 'case-return-delivery-voice', 3, '문자 링크 접속 및 앱 설치를 요구한다.', TRUE),
    ('case-return-delivery-message-option-1', 'case-return-delivery-message', 1, '반품 신청 후 문자가 왔다.', FALSE),
    ('case-return-delivery-message-option-2', 'case-return-delivery-message', 2, '공식 홈페이지가 아닌 단축 URL 링크를 보냈다.', TRUE),
    ('case-return-delivery-message-option-3', 'case-return-delivery-message', 3, '개인정보를 다시 입력하도록 요구했다.', TRUE),
    ('case-return-delivery-message-option-4', 'case-return-delivery-message', 4, '택배 관련 문자가 왔다.', FALSE),
    ('case-fire-agency-message-option-1', 'case-fire-agency-message', 1, '최근 소방시설법이 개정되었다고 안내했다.', FALSE),
    ('case-fire-agency-message-option-2', 'case-fire-agency-message', 2, '소방 안전점검이 예정되어 있다고 말했다.', FALSE),
    ('case-fire-agency-message-option-3', 'case-fire-agency-message', 3, '특정 업체에서만 장비를 구매하라고 안내했다.', TRUE),
    ('case-fire-agency-message-option-4', 'case-fire-agency-message', 4, '정부 지원금을 받을 수 있다고 설명했다.', FALSE),
    ('case-special-investment-message-option-1', 'case-special-investment-message', 1, 'AI 반도체 투자 상품이라고 소개했다.', FALSE),
    ('case-special-investment-message-option-2', 'case-special-investment-message', 2, '오늘까지만 가입할 수 있다고 안내했다.', FALSE),
    ('case-special-investment-message-option-3', 'case-special-investment-message', 3, '원금 보장과 월 20% 수익을 약속했다.', TRUE),
    ('case-special-investment-message-option-4', 'case-special-investment-message', 4, 'VIP 고객 대상으로 안내했다고 말했다.', FALSE),
    ('case-new-number-family-transfer-voice-option-1', 'case-new-number-family-transfer-voice', 1, '휴대폰이 고장 났다고 말했다.', FALSE),
    ('case-new-number-family-transfer-voice-option-2', 'case-new-number-family-transfer-voice', 2, '급하게 결제가 필요하다고 말했다.', FALSE),
    ('case-new-number-family-transfer-voice-option-3', 'case-new-number-family-transfer-voice', 3, '새 번호로 연락해 즉시 송금을 요청했다.', TRUE),
    ('case-new-number-family-transfer-voice-option-4', 'case-new-number-family-transfer-voice', 4, '내일 돈을 돌려주겠다고 말했다.', FALSE),
    ('case-new-number-family-transfer-message-option-1', 'case-new-number-family-transfer-message', 1, '휴대폰이 고장 나 새 번호로 연락했다고 말했다.', FALSE),
    ('case-new-number-family-transfer-message-option-2', 'case-new-number-family-transfer-message', 2, '결제가 급하다고 말했다.', FALSE),
    ('case-new-number-family-transfer-message-option-3', 'case-new-number-family-transfer-message', 3, '통화는 안 된다며 메신저로만 송금을 요청했다.', TRUE),
    ('case-new-number-family-transfer-message-option-4', 'case-new-number-family-transfer-message', 4, '내일 돈을 돌려주겠다고 말했다.', FALSE)
ON CONFLICT (option_id) DO NOTHING;

UPDATE case_variant_options
SET option_text = '휴대폰이 고장 나서 다른 번호로 연락했다고 말했다.',
    is_correct = FALSE
WHERE option_id = 'case-mobile-repair-voice-option-1';

UPDATE case_variant_options
SET option_text = '병원에서 치료를 받고 있다고 말했다.',
    is_correct = FALSE
WHERE option_id = 'case-mobile-repair-voice-option-2';

UPDATE case_variant_options
SET option_text = '병원비를 개인 계좌로 바로 송금해 달라고 했다.',
    is_correct = TRUE
WHERE option_id = 'case-mobile-repair-voice-option-3';

UPDATE case_variant_options
SET option_text = '간호사가 기다리고 있다고 말했다.',
    is_correct = FALSE
WHERE option_id = 'case-mobile-repair-voice-option-4';
