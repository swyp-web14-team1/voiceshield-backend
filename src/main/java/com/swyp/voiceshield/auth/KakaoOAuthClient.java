package com.swyp.voiceshield.auth;

public interface KakaoOAuthClient {

    KakaoUserProfile retrieveUserProfile(String authorizationCode);
}
