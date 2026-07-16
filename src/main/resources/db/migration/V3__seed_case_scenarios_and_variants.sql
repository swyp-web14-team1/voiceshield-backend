INSERT INTO categories (category_id, category_name)
VALUES
    ('category-institution-impersonation', '기관 사칭'),
    ('category-delivery-impersonation', '택배 사칭'),
    ('category-investment-fraud', '투자 사기');

INSERT INTO case_scenarios (scenario_id, case_name)
VALUES
    ('case-mobile-repair', '휴대폰 고장'),
    ('case-return-delivery', '반품 택배'),
    ('case-fire-agency', '소방기관 사칭'),
    ('case-special-investment', '특별 투자상품 권유');

INSERT INTO case_variants (variant_id, scenario_id, channel)
VALUES
    ('case-mobile-repair-message', 'case-mobile-repair', 'MESSAGE'),
    ('case-mobile-repair-voice', 'case-mobile-repair', 'VOICE'),
    ('case-return-delivery-message', 'case-return-delivery', 'MESSAGE'),
    ('case-return-delivery-voice', 'case-return-delivery', 'VOICE'),
    ('case-fire-agency-message', 'case-fire-agency', 'MESSAGE'),
    ('case-fire-agency-voice', 'case-fire-agency', 'VOICE'),
    ('case-special-investment-message', 'case-special-investment', 'MESSAGE'),
    ('case-special-investment-voice', 'case-special-investment', 'VOICE');
