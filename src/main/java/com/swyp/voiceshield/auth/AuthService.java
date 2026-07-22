package com.swyp.voiceshield.auth;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final String KAKAO_AUTHENTICATED = "KAKAO_AUTHENTICATED";

    private final KakaoOAuthClient kakaoOAuthClient;

    public AuthService(KakaoOAuthClient kakaoOAuthClient) {
        this.kakaoOAuthClient = kakaoOAuthClient;
    }

    public KakaoLoginResponse loginWithKakao(String kakaoAuthCode) {
        KakaoUserProfile kakaoProfile = retrieveKakaoProfile(kakaoAuthCode);
        return new KakaoLoginResponse(kakaoProfile.providerUserId(), KAKAO_AUTHENTICATED);
    }

    private KakaoUserProfile retrieveKakaoProfile(String kakaoAuthCode) {
        try {
            return kakaoOAuthClient.retrieveUserProfile(kakaoAuthCode);
        } catch (RuntimeException exception) {
            throw new ApiException(ErrorCode.KAKAO_AUTH_FAILED);
        }
    }
}
