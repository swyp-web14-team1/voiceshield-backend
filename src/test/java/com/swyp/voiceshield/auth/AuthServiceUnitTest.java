package com.swyp.voiceshield.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.swyp.voiceshield.user.AppUser;
import com.swyp.voiceshield.user.AppUserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private KakaoOAuthClient kakaoOAuthClient;

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginWithKakaoRestoresSoftDeletedUser() {
        AppUser user = AppUser.createKakao("kakao-withdrawn-user", LocalDateTime.now());
        user.completeSignup();
        user.markWithdrawn(LocalDateTime.now());

        when(kakaoOAuthClient.retrieveUserProfile("withdrawn-user-code"))
                .thenReturn(new KakaoUserProfile("kakao-withdrawn-user", "복구이름", "복구닉네임"));
        when(appUserRepository.findByProviderAndProviderUserId("KAKAO", "kakao-withdrawn-user"))
                .thenReturn(Optional.of(user));

        KakaoLoginResult result = authService.loginWithKakao("withdrawn-user-code");

        assertThat(result.created()).isFalse();
        assertThat(result.response().userId()).isEqualTo(user.getUserId());
        assertThat(user.isDeleted()).isFalse();
        assertThat(user.getName()).isEqualTo("복구이름");
        assertThat(user.getNickname()).isEqualTo("복구닉네임");
    }
}
