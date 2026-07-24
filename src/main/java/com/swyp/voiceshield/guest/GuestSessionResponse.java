package com.swyp.voiceshield.guest;

import java.util.List;

public record GuestSessionResponse(
        String guestSessionId,
        List<String> availableFeatures
) {
}
