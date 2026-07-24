package com.swyp.voiceshield.auth;

public record KakaoLoginResponse(
        String userId,
        String loginResult,
        String signupStatus
) {
}
