package com.swyp.voiceshield;

import com.swyp.voiceshield.auth.KakaoOAuthClient;
import com.swyp.voiceshield.auth.KakaoUserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void kakaoLoginReturnsKakaoProviderUserIdWhenAuthorizationCodeIsValid() throws Exception {
        mockMvc.perform(post("/api/v1/auth/kakao")
                        .contentType("application/json")
                        .content("{\"kakaoAuthCode\":\"valid-kakao-code\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.kakaoProviderUserId").value("kakao-valid-kakao-code"))
                .andExpect(jsonPath("$.data.loginResult").value("KAKAO_AUTHENTICATED"));
    }

    @Test
    void kakaoLoginRejectsInvalidAuthorizationCode() throws Exception {
        mockMvc.perform(post("/api/v1/auth/kakao")
                        .contentType("application/json")
                        .content("{\"kakaoAuthCode\":\"invalid-kakao-code\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH-002"));
    }

    @Test
    void kakaoLoginRejectsBlankAuthorizationCode() throws Exception {
        mockMvc.perform(post("/api/v1/auth/kakao")
                        .contentType("application/json")
                        .content("{\"kakaoAuthCode\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON-001"));
    }

    @TestConfiguration
    static class KakaoOAuthClientTestConfig {

        @Bean
        @Primary
        KakaoOAuthClient kakaoOAuthClient() {
            return authorizationCode -> {
                if ("invalid-kakao-code".equals(authorizationCode)) {
                    throw new IllegalArgumentException("Invalid Kakao authorization code");
                }
                return new KakaoUserProfile("kakao-" + authorizationCode);
            };
        }
    }
}
