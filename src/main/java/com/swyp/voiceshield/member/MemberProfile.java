package com.swyp.voiceshield.member;

import com.swyp.voiceshield.user.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "member_profiles")
public class MemberProfile {

    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "signup_status", nullable = false)
    private String signupStatus;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected MemberProfile() {
    }

    private MemberProfile(String memberId, AppUser user, String signupStatus, LocalDateTime createdAt) {
        this.memberId = memberId;
        this.user = user;
        this.signupStatus = signupStatus;
        this.createdAt = createdAt;
    }

    public static MemberProfile create(AppUser user, String signupStatus, LocalDateTime now) {
        return new MemberProfile("member-" + UUID.randomUUID(), user, signupStatus, now);
    }

    public String getMemberId() {
        return memberId;
    }

    public AppUser getUser() {
        return user;
    }

    public String getSignupStatus() {
        return signupStatus;
    }
}
