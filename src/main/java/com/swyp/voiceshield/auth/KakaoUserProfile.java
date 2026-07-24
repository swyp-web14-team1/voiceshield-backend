package com.swyp.voiceshield.auth;

public record KakaoUserProfile(
        String providerUserId,
        String name,
        String nickname
) {
}
