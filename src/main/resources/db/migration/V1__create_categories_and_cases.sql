CREATE TABLE categories (
    category_id VARCHAR(255) PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL UNIQUE,
    CONSTRAINT chk_categories_name CHECK (
        category_name IN ('기관 사칭', '가족 사칭', '택배 사칭', '메신저 피싱', '투자 사기')
    )
);

CREATE TABLE cases (
    case_id VARCHAR(255) PRIMARY KEY,
    category_id VARCHAR(255) NOT NULL,
    case_name VARCHAR(255) NOT NULL,
    difficulty VARCHAR(255),
    estimated_learning_time VARCHAR(255),
    completion_rate VARCHAR(255),
    CONSTRAINT fk_cases_category
        FOREIGN KEY (category_id) REFERENCES categories (category_id)
);
