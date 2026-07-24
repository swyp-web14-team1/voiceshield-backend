package com.swyp.voiceshield.guest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "guest_sessions")
public class GuestSession {

    @Id
    @Column(name = "guest_session_id", nullable = false)
    private String guestSessionId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected GuestSession() {
    }

    private GuestSession(String guestSessionId, LocalDateTime createdAt) {
        this.guestSessionId = guestSessionId;
        this.createdAt = createdAt;
    }

    public static GuestSession create(String guestSessionId, LocalDateTime createdAt) {
        return new GuestSession(guestSessionId, createdAt);
    }

    public String getGuestSessionId() {
        return guestSessionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
