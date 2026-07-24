package com.swyp.voiceshield.guest;

import com.swyp.voiceshield.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/guest-sessions")
public class GuestSessionController {

    private final GuestSessionService guestSessionService;

    public GuestSessionController(GuestSessionService guestSessionService) {
        this.guestSessionService = guestSessionService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GuestSessionResponse>> createGuestSession() {
        GuestSessionResponse response = guestSessionService.createGuestSession();
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }
}
