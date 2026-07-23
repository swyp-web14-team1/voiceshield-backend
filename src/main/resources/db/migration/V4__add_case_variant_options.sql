CREATE TABLE case_variant_options (
    option_id VARCHAR(255) PRIMARY KEY,
    variant_id VARCHAR(255) NOT NULL,
    option_number INTEGER NOT NULL,
    option_text TEXT NOT NULL,
    is_correct BOOLEAN NOT NULL,
    CONSTRAINT fk_case_variant_options_variant
        FOREIGN KEY (variant_id) REFERENCES case_variants (variant_id),
    CONSTRAINT uk_case_variant_options_variant_number
        UNIQUE (variant_id, option_number)
);

UPDATE case_variants
SET content = '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"엄마... 나야.' || CHR(10) ||
              '휴대폰이 고장 나서' || CHR(10) ||
              '친구 전화로 연락하는 거야."' || CHR(10) ||
              '나' || CHR(10) ||
              '"무슨 일이야?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"아까 길에서 넘어져서' || CHR(10) ||
              '응급실에 왔는데...' || CHR(10) ||
              '보험 처리 전에' || CHR(10) ||
              '병원비를 먼저 내야 한대."' || CHR(10) ||
              '나' || CHR(10) ||
              '"많이 다친 거야?"' || CHR(10) ||
              '사기범' || CHR(10) ||
              '"괜찮은데...' || CHR(10) ||
              '지금 간호사가 기다리고 있어.' || CHR(10) ||
              '내 번호로는 연락 안 되고' || CHR(10) ||
              '이 번호로만 통화할 수 있어.' || CHR(10) ||
              '병원 계좌로 20만 원만' || CHR(10) ||
              '먼저 보내줘."'
WHERE variant_id = 'case-mobile-repair-voice';

INSERT INTO case_variant_options (option_id, variant_id, option_number, option_text, is_correct)
VALUES
    ('case-mobile-repair-voice-option-1', 'case-mobile-repair-voice', 1, '휴대폰이 고장 나서 다른 번호로 연락했다고 말했다.', FALSE),
    ('case-mobile-repair-voice-option-2', 'case-mobile-repair-voice', 2, '병원에서 치료를 받고 있다고 말했다.', FALSE),
    ('case-mobile-repair-voice-option-3', 'case-mobile-repair-voice', 3, '병원비를 개인 계좌로 바로 송금해 달라고 했다.', TRUE),
    ('case-mobile-repair-voice-option-4', 'case-mobile-repair-voice', 4, '간호사가 기다리고 있다고 말했다.', FALSE),
    ('case-mobile-repair-message-option-1', 'case-mobile-repair-message', 1, '새로운 번호 또는 임시폰으로 연락했다.', FALSE),
    ('case-mobile-repair-message-option-2', 'case-mobile-repair-message', 2, '병원비를 개인 계좌로 바로 송금해 달라고 했다.', TRUE),
    ('case-mobile-repair-message-option-3', 'case-mobile-repair-message', 3, '기존 번호로는 연락하지 말라고 했다.', FALSE),
    ('case-mobile-repair-message-option-4', 'case-mobile-repair-message', 4, '다쳤다고 말했다.', FALSE),
    ('case-return-delivery-voice-option-1', 'case-return-delivery-voice', 1, '택배 기사가 방문 전 전화한다.', FALSE),
    ('case-return-delivery-voice-option-2', 'case-return-delivery-voice', 2, '개인 휴대전화 번호로 주소를 다시 요구한다.', TRUE),
    ('case-return-delivery-voice-option-3', 'case-return-delivery-voice', 3, '문자 링크 접속 및 앱 설치를 요구한다.', TRUE);
