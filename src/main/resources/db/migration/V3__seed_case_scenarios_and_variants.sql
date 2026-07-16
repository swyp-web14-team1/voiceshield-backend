ALTER TABLE categories DROP CONSTRAINT chk_categories_name;

ALTER TABLE categories
    ADD CONSTRAINT chk_categories_name CHECK (
        category_name IN ('기관 사칭', '가족 사칭', '택배 사칭', '메신저 피싱', '투자 사기', '휴대폰 고장')
    );

INSERT INTO categories (category_id, category_name)
VALUES
    ('category-institution-impersonation', '기관 사칭'),
    ('category-delivery-impersonation', '택배 사칭'),
    ('category-investment-fraud', '투자 사기'),
    ('category-mobile-repair', '휴대폰 고장');

INSERT INTO case_scenarios (scenario_id, category_id, case_name)
VALUES
    ('case-mobile-repair', 'category-mobile-repair', '휴대폰 고장'),
    ('case-return-delivery', 'category-delivery-impersonation', '반품 택배'),
    ('case-fire-agency', 'category-institution-impersonation', '소방기관 사칭'),
    ('case-special-investment', 'category-investment-fraud', '특별 투자상품 권유');

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
