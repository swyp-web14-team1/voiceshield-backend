INSERT INTO categories (category_id, category_name)
SELECT 'category-family-impersonation', '가족 사칭'
WHERE NOT EXISTS (
    SELECT 1
    FROM categories
    WHERE category_id = 'category-family-impersonation'
);

UPDATE case_scenarios
SET category_id = 'category-family-impersonation'
WHERE scenario_id = 'case-mobile-repair';

UPDATE case_scenarios
SET category_id = 'category-delivery-impersonation'
WHERE scenario_id = 'case-return-delivery';

UPDATE case_scenarios
SET category_id = 'category-institution-impersonation'
WHERE scenario_id = 'case-fire-agency';

UPDATE case_scenarios
SET category_id = 'category-investment-fraud'
WHERE scenario_id = 'case-special-investment';
