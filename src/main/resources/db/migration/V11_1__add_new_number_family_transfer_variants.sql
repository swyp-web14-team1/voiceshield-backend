INSERT INTO case_scenarios (scenario_id, case_name)
VALUES ('case-new-number-family-transfer', '새 번호로 온 가족의 송금 요청')
ON CONFLICT (scenario_id) DO NOTHING;

INSERT INTO case_variants (variant_id, scenario_id, channel)
VALUES
    ('case-new-number-family-transfer-message', 'case-new-number-family-transfer', 'MESSAGE'),
    ('case-new-number-family-transfer-voice', 'case-new-number-family-transfer', 'VOICE')
ON CONFLICT (variant_id) DO NOTHING;

UPDATE case_variants
SET content = '모르는 번호로 전화가 걸려왔습니다.' || CHR(10) ||
              '상대는 자녀라고 자신을 소개하며 휴대폰이 고장 나 새 번호로 연락했다고 말합니다. 이어서 급하게 결제할 일이 생겼다며 대신 송금을 부탁합니다.' || CHR(10) ||
              '[전화벨]' || CHR(10) ||
              '나' || CHR(10) ||
              '"여보세요?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"엄마(아빠), 나야.' || CHR(10) ||
              '휴대폰이 갑자기 고장 나서' || CHR(10) ||
              '새 번호로 연락했어."' || CHR(10) ||
              '나' || CHR(10) ||
              '"무슨 일이야?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"지금 급하게 결제해야 하는데' || CHR(10) ||
              '계좌이체가 안 돼.' || CHR(10) ||
              '85만 원만 먼저 보내줄 수 있어?' || CHR(10) ||
              '내일 바로 돌려줄게."' || CHR(10) ||
              '나' || CHR(10) ||
              '"기존 번호는 어떻게 된 거야?"' || CHR(10) ||
              '상대' || CHR(10) ||
              '"지금 배터리도 거의 없고' || CHR(10) ||
              '오래 통화하기 어려워.' || CHR(10) ||
              '내가 계좌번호 문자로 보내줄게.' || CHR(10) ||
              '부탁해."'
WHERE variant_id = 'case-new-number-family-transfer-voice';

UPDATE case_variants
SET content = '메신저로 자녀를 사칭한 사람이 급하게 연락을 보내왔습니다.' || CHR(10) ||
              '휴대폰이 고장 났다며 새 번호로 연락 중이라고 하고, 급한 결제가 필요하다며 대신 송금을 부탁합니다.' || CHR(10) ||
              '딸' || CHR(10) ||
              '엄마 나야.' || CHR(10) ||
              '휴대폰이 갑자기 고장 나서' || CHR(10) ||
              '새 번호로 연락해.' || CHR(10) ||
              '기존 번호는 지금 사용할 수 없어.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '딸' || CHR(10) ||
              '지금 급하게 결제해야 하는데' || CHR(10) ||
              '계좌이체가 안 돼.' || CHR(10) ||
              '엄마가 대신 85만 원만' || CHR(10) ||
              '먼저 보내줄 수 있어?' || CHR(10) ||
              '내일 바로 돌려줄게.' || CHR(10) ||
              '----------------------------' || CHR(10) ||
              '딸' || CHR(10) ||
              '지금 통화는 안 돼.' || CHR(10) ||
              '메신저로만 부탁할게.' || CHR(10) ||
              '계좌번호 보낼게.'
WHERE variant_id = 'case-new-number-family-transfer-message';

