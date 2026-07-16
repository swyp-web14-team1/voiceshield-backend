UPDATE case_scenarios
SET case_name = '휴대폰 고장'
WHERE scenario_id = 'case-mobile-repair';

UPDATE case_scenarios
SET case_name = '반품 택배'
WHERE scenario_id = 'case-return-delivery';

UPDATE case_variants
SET content = '[전화벨]' || CHAR(10) ||
              '나' || CHAR(10) ||
              '"여보세요?"' || CHAR(10) ||
              '사기범' || CHAR(10) ||
              '"엄마... 나야.' || CHAR(10) ||
              '휴대폰이 고장 나서' || CHAR(10) ||
              '친구 전화로 연락하는 거야."' || CHAR(10) ||
              '나' || CHAR(10) ||
              '"무슨 일이야?"' || CHAR(10) ||
              '사기범' || CHAR(10) ||
              '"아까 길에서 넘어져서' || CHAR(10) ||
              '응급실에 왔는데...' || CHAR(10) ||
              '보험 처리 전에' || CHAR(10) ||
              '병원비를 먼저 내야 한대."' || CHAR(10) ||
              '나' || CHAR(10) ||
              '"많이 다친 거야?"' || CHAR(10) ||
              '사기범' || CHAR(10) ||
              '"괜찮은데...' || CHAR(10) ||
              '지금 간호사가 기다리고 있어.' || CHAR(10) ||
              '내 번호로는 연락 안 되고' || CHAR(10) ||
              '이 번호로만 통화할 수 있어.' || CHAR(10) ||
              '병원 계좌로 20만 원만' || CHAR(10) ||
              '먼저 보내줘."'
WHERE variant_id = 'case-mobile-repair-voice';

UPDATE case_variant_options
SET option_text = '계좌번호를 받아 바로 송금한다.'
WHERE option_id = 'case-mobile-repair-voice-option-1';

UPDATE case_variant_options
SET option_text = '기존에 저장된 아들 번호로 직접 전화한다.'
WHERE option_id = 'case-mobile-repair-voice-option-2';

UPDATE case_variant_options
SET option_text = '상대방이 알려준 계좌로 송금한다.'
WHERE option_id = 'case-mobile-repair-voice-option-3';

UPDATE case_variant_options
SET option_text = '전화를 끊고 다른 가족에게 먼저 확인한다.'
WHERE option_id = 'case-mobile-repair-voice-option-4';
