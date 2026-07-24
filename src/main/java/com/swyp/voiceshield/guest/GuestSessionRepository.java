package com.swyp.voiceshield.guest;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestSessionRepository extends JpaRepository<GuestSession, String> {
}
