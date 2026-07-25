ALTER TABLE case_scenarios
    ADD COLUMN average_damage_amount VARCHAR(255);

ALTER TABLE case_scenarios
    ADD COLUMN report_count VARCHAR(255);

UPDATE case_scenarios
SET average_damage_amount = '약 1700만원',
    report_count = '약 59,565건 (2023년 기준)'
WHERE scenario_id = 'case-mobile-repair';

UPDATE case_scenarios
SET average_damage_amount = '수백만 원 규모',
    report_count = '약 91,159건 (2023년 기준)'
WHERE scenario_id = 'case-return-delivery';

UPDATE case_scenarios
SET average_damage_amount = '약 4,426만 원',
    report_count = '약 350,010건 (2023년 기준)'
WHERE scenario_id = 'case-fire-agency';

UPDATE case_scenarios
SET average_damage_amount = '수천만 원 규모',
    report_count = '약 12,851건 (2024년 기준)'
WHERE scenario_id = 'case-special-investment';

UPDATE case_scenarios
SET average_damage_amount = '약 954만 원',
    report_count = '약 13,179건 (2023년 기준)'
WHERE scenario_id = 'case-new-number-family-transfer';
