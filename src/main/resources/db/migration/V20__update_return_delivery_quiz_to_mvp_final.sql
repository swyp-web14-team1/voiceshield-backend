UPDATE case_variant_quizzes
SET question = '다음 중 택배 사기 문자임을 가장 강하게 의심할 수 있는 단서는 무엇인가요?',
    explanation = '정상적인 택배사는 문자 링크를 통해 앱 설치를 요구하지 않습니다. 출처가 불분명한 링크를 클릭하거나 앱을 설치하면 악성 앱이 설치되어 개인정보와 금융정보가 탈취될 수 있습니다.'
WHERE variant_id IN ('case-return-delivery-voice', 'case-return-delivery-message')
  AND quiz_number = 1;

UPDATE case_variant_options
SET option_text = '택배 기사가 방문 전에 전화를 한다.',
    is_correct = FALSE
WHERE option_id = 'case-return-delivery-voice-option-1';

UPDATE case_variant_options
SET option_text = '문자 링크 접속 후 앱 설치를 요구한다.',
    is_correct = TRUE
WHERE option_id = 'case-return-delivery-voice-option-2';

UPDATE case_variant_options
SET option_text = '배송이 하루 정도 지연되었다고 안내한다.',
    is_correct = FALSE
WHERE option_id = 'case-return-delivery-voice-option-3';

INSERT INTO case_variant_options (option_id, variant_id, option_number, option_text, is_correct)
VALUES ('case-return-delivery-voice-option-4', 'case-return-delivery-voice', 4, '배송 주소를 다시 한 번 확인해 달라고 요청한다.', FALSE)
ON CONFLICT (option_id) DO UPDATE
SET option_text = EXCLUDED.option_text,
    is_correct = EXCLUDED.is_correct;

UPDATE case_variant_options
SET option_text = '택배 기사가 방문 전에 전화를 한다.',
    is_correct = FALSE
WHERE option_id = 'case-return-delivery-message-option-1';

UPDATE case_variant_options
SET option_text = '문자 링크 접속 후 앱 설치를 요구한다.',
    is_correct = TRUE
WHERE option_id = 'case-return-delivery-message-option-2';

UPDATE case_variant_options
SET option_text = '배송이 하루 정도 지연되었다고 안내한다.',
    is_correct = FALSE
WHERE option_id = 'case-return-delivery-message-option-3';

UPDATE case_variant_options
SET option_text = '배송 주소를 다시 한 번 확인해 달라고 요청한다.',
    is_correct = FALSE
WHERE option_id = 'case-return-delivery-message-option-4';
