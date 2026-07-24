package com.swyp.voiceshield.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "provider", nullable = false)
    private String provider;

    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;

    @Column(name = "signup_status", nullable = false)
    private String signupStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_login_at", nullable = false)
    private LocalDateTime lastLoginAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    protected AppUser() {
    }

    private AppUser(
            String userId,
            String provider,
            String providerUserId,
            String signupStatus,
            LocalDateTime createdAt,
            LocalDateTime lastLoginAt,
            LocalDateTime deletedAt
    ) {
        this.userId = userId;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.signupStatus = signupStatus;
        this.createdAt = createdAt;
        this.lastLoginAt = lastLoginAt;
        this.deletedAt = deletedAt;
    }

    public static AppUser createKakao(String providerUserId, LocalDateTime now) {
        return new AppUser(
                "user-" + UUID.randomUUID(),
                "KAKAO",
                providerUserId,
                "SIGNUP_REQUIRED",
                now,
                now,
                null
        );
    }

    public void markLoggedIn(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void markWithdrawn(LocalDateTime now) {
        this.deletedAt = now;
    }

    public void restore(LocalDateTime now) {
        this.deletedAt = null;
        this.lastLoginAt = now;
    }

    public void completeSignup() {
        this.signupStatus = "SIGNUP_COMPLETE";
    }

    public String getUserId() {
        return userId;
    }

    public String getSignupStatus() {
        return signupStatus;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }
}
