package com.swyp.voiceshield.auth;

import jakarta.validation.constraints.NotBlank;

public record KakaoLoginRequest(
        @NotBlank String kakaoAuthCode
) {
}
