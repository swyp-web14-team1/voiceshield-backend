package com.swyp.voiceshield.guest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuestSessionService {

    private static final List<String> AVAILABLE_FEATURES = List.of(
            "CATEGORY_VIEW",
            "CASE_DETAIL_VIEW",
            "REPORT_GUIDE_VIEW"
    );

    private final GuestSessionRepository guestSessionRepository;

    public GuestSessionService(GuestSessionRepository guestSessionRepository) {
        this.guestSessionRepository = guestSessionRepository;
    }

    @Transactional
    public GuestSessionResponse createGuestSession() {
        String guestSessionId = "guest-" + UUID.randomUUID();
        GuestSession guestSession = GuestSession.create(guestSessionId, LocalDateTime.now());
        GuestSession savedGuestSession = guestSessionRepository.save(guestSession);
        return new GuestSessionResponse(savedGuestSession.getGuestSessionId(), AVAILABLE_FEATURES);
    }
}
