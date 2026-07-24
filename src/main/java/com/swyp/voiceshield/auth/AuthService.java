package com.swyp.voiceshield.auth;

import com.swyp.voiceshield.exception.ApiException;
import com.swyp.voiceshield.exception.ErrorCode;
import com.swyp.voiceshield.user.AppUser;
import com.swyp.voiceshield.user.AppUserRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private static final String KAKAO_PROVIDER = "KAKAO";
    private static final String LOGIN_COMPLETE = "LOGIN_COMPLETE";

    private final KakaoOAuthClient kakaoOAuthClient;
    private final AppUserRepository appUserRepository;

    public AuthService(KakaoOAuthClient kakaoOAuthClient, AppUserRepository appUserRepository) {
        this.kakaoOAuthClient = kakaoOAuthClient;
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public KakaoLoginResult loginWithKakao(String kakaoAuthCode) {
        KakaoUserProfile kakaoProfile = retrieveKakaoProfile(kakaoAuthCode);
        return appUserRepository.findByProviderAndProviderUserId(KAKAO_PROVIDER, kakaoProfile.providerUserId())
                .map(user -> toResult(loginExistingUser(user), false))
                .orElseGet(() -> toResult(createKakaoUser(kakaoProfile), true));
    }

    private KakaoUserProfile retrieveKakaoProfile(String kakaoAuthCode) {
        try {
            return kakaoOAuthClient.retrieveUserProfile(kakaoAuthCode);
        } catch (RuntimeException exception) {
            throw new ApiException(ErrorCode.KAKAO_AUTH_FAILED);
        }
    }

    private AppUser loginExistingUser(AppUser user) {
        user.markLoggedIn(LocalDateTime.now());
        return user;
    }

    private AppUser createKakaoUser(KakaoUserProfile kakaoProfile) {
        return appUserRepository.save(AppUser.createKakao(kakaoProfile.providerUserId(), LocalDateTime.now()));
    }

    private KakaoLoginResult toResult(AppUser user, boolean created) {
        KakaoLoginResponse response = new KakaoLoginResponse(
                user.getUserId(),
                LOGIN_COMPLETE,
                user.getSignupStatus()
        );
        return new KakaoLoginResult(response, created);
    }
}
