package com.swyp.voiceshield;

import com.swyp.voiceshield.auth.KakaoOAuthClient;
import com.swyp.voiceshield.auth.KakaoUserProfile;
import com.swyp.voiceshield.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void kakaoLoginCreatesUserOnFirstEntry() throws Exception {
        mockMvc.perform(post("/api/v1/auth/kakao")
                        .contentType("application/json")
                        .content("{\"kakaoAuthCode\":\"first-entry-code\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId", notNullValue()))
                .andExpect(jsonPath("$.data.loginResult").value("LOGIN_COMPLETE"))
                .andExpect(jsonPath("$.data.signupStatus").value("SIGNUP_REQUIRED"));

        assertThat(appUserRepository.findByProviderAndProviderUserId("KAKAO", "kakao-first-entry-code"))
                .isPresent();
    }

    @Test
    void kakaoLoginReturnsExistingUserOnRepeatedEntry() throws Exception {
        String requestBody = "{\"kakaoAuthCode\":\"repeat-entry-code\"}";

        String firstResponse = mockMvc.perform(post("/api/v1/auth/kakao")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String userId = firstResponse.replaceAll(".*\\\"userId\\\":\\\"([^\\\"]+)\\\".*", "$1");

        mockMvc.perform(post("/api/v1/auth/kakao")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.loginResult").value("LOGIN_COMPLETE"))
                .andExpect(jsonPath("$.data.signupStatus").value("SIGNUP_REQUIRED"));
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
