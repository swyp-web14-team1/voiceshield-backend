CREATE TABLE app_users (
    user_id VARCHAR(255) PRIMARY KEY,
    provider VARCHAR(50) NOT NULL,
    provider_user_id VARCHAR(255) NOT NULL,
    signup_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    last_login_at TIMESTAMP NOT NULL,
    CONSTRAINT uk_app_users_provider_user UNIQUE (provider, provider_user_id),
    CONSTRAINT chk_app_users_provider CHECK (provider IN ('KAKAO')),
    CONSTRAINT chk_app_users_signup_status CHECK (signup_status IN ('SIGNUP_REQUIRED', 'SIGNUP_COMPLETE'))
);
