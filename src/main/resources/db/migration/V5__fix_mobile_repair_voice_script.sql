UPDATE case_scenarios
SET case_name = '휴대폰 고장'
WHERE scenario_id = 'case-mobile-repair';

UPDATE case_scenarios
SET case_name = '반품 택배'
WHERE scenario_id = 'case-return-delivery';

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
