CREATE TABLE case_scenarios (
    scenario_id VARCHAR(255) PRIMARY KEY,
    category_id VARCHAR(255),
    case_name VARCHAR(255) NOT NULL,
    difficulty VARCHAR(255),
    estimated_learning_time VARCHAR(255),
    completion_rate VARCHAR(255),
    CONSTRAINT fk_case_scenarios_category
        FOREIGN KEY (category_id) REFERENCES categories (category_id)
);

INSERT INTO case_scenarios (
    scenario_id,
    category_id,
    case_name,
    difficulty,
    estimated_learning_time,
    completion_rate
)
SELECT
    case_id,
    category_id,
    case_name,
    difficulty,
    estimated_learning_time,
    completion_rate
FROM cases;

CREATE TABLE case_variants (
    variant_id VARCHAR(255) PRIMARY KEY,
    scenario_id VARCHAR(255) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    content TEXT,
    CONSTRAINT fk_case_variants_scenario
        FOREIGN KEY (scenario_id) REFERENCES case_scenarios (scenario_id),
    CONSTRAINT chk_case_variants_channel
        CHECK (channel IN ('MESSAGE', 'VOICE')),
    CONSTRAINT uk_case_variants_scenario_channel
        UNIQUE (scenario_id, channel)
);

DROP TABLE cases;
