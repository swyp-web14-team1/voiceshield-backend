CREATE TABLE member_profiles (
    member_id VARCHAR(255) PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    signup_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_member_profiles_user
        FOREIGN KEY (user_id) REFERENCES app_users (user_id),
    CONSTRAINT chk_member_profiles_signup_status CHECK (signup_status IN ('SIGNUP_COMPLETE'))
);
