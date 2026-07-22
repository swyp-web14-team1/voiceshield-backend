package com.swyp.voiceshield.auth;

public record KakaoLoginResponse(
        String kakaoProviderUserId,
        String loginResult
) {
}
