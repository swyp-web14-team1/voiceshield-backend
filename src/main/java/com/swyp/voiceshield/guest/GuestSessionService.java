package com.swyp.voiceshield.guest;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class GuestSessionService {

    private static final List<String> AVAILABLE_FEATURES = List.of(
            "CATEGORY_VIEW",
            "CASE_DETAIL_VIEW",
            "REPORT_GUIDE_VIEW"
    );

    public GuestSessionResponse createGuestSession() {
        return new GuestSessionResponse("guest-" + UUID.randomUUID(), AVAILABLE_FEATURES);
    }
}
