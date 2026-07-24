package com.swyp.voiceshield;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.voiceshield.member.MemberProfile;
import com.swyp.voiceshield.member.MemberProfileRepository;
import com.swyp.voiceshield.user.AppUser;
import com.swyp.voiceshield.user.AppUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private MemberProfileRepository memberProfileRepository;

    @Test
    void createMemberStoresBasicUserInformationAndCompletesSignup() throws Exception {
        AppUser user = appUserRepository.save(AppUser.createKakao("kakao-member-create", LocalDateTime.now()));

        String responseBody = mockMvc.perform(post("/api/v1/members")
                        .contentType("application/json")
                        .content("""
                                {
                                  "userId": "%s",
                                  "signupStatus": "SIGNUP_COMPLETE"
                                }
                                """.formatted(user.getUserId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.memberId", notNullValue()))
                .andExpect(jsonPath("$.data.signupStatus").value("SIGNUP_COMPLETE"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode responseJson = objectMapper.readTree(responseBody);
        String memberId = responseJson.path("data").path("memberId").asText();

        assertThat(memberId).startsWith("member-");
        assertThat(appUserRepository.findById(user.getUserId()))
                .isPresent()
                .get()
                .extracting(AppUser::getSignupStatus)
                .isEqualTo("SIGNUP_COMPLETE");
    }

    @Test
    void getMyMemberProfileIdentifiesCurrentUserByUserIdHeader() throws Exception {
        AppUser user = appUserRepository.save(AppUser.createKakao("kakao-member-me", LocalDateTime.now()));
        user.completeSignup();
        MemberProfile memberProfile = memberProfileRepository.save(
                MemberProfile.create(user, "SIGNUP_COMPLETE", LocalDateTime.now())
        );

        mockMvc.perform(get("/api/v1/members/me")
                        .header("X-User-Id", user.getUserId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.memberId").value(memberProfile.getMemberId()))
                .andExpect(jsonPath("$.data.userId").value(user.getUserId()))
                .andExpect(jsonPath("$.data.signupStatus").value("SIGNUP_COMPLETE"));
    }

    @Test
    void getMyMemberProfileRequiresUserIdHeader() throws Exception {
        mockMvc.perform(get("/api/v1/members/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH-001"));
    }

    @Test
    void withdrawCurrentMemberSoftDeletesUser() throws Exception {
        AppUser user = appUserRepository.save(AppUser.createKakao("kakao-member-withdraw", LocalDateTime.now()));
        user.completeSignup();
        MemberProfile memberProfile = memberProfileRepository.save(
                MemberProfile.create(user, "SIGNUP_COMPLETE", LocalDateTime.now())
        );

        mockMvc.perform(delete("/api/v1/members/me")
                        .header("X-User-Id", user.getUserId()))
                .andExpect(status().isNoContent());

        assertThat(memberProfileRepository.findById(memberProfile.getMemberId())).isPresent();
        assertThat(appUserRepository.findById(user.getUserId()))
                .isPresent()
                .get()
                .extracting(AppUser::getDeletedAt)
                .isNotNull();
    }

    @Test
    void getMyMemberProfileReturnsNotFoundAfterWithdrawal() throws Exception {
        AppUser user = appUserRepository.save(AppUser.createKakao("kakao-member-withdraw-me", LocalDateTime.now()));
        user.completeSignup();
        memberProfileRepository.save(
                MemberProfile.create(user, "SIGNUP_COMPLETE", LocalDateTime.now())
        );

        mockMvc.perform(delete("/api/v1/members/me")
                        .header("X-User-Id", user.getUserId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/members/me")
                        .header("X-User-Id", user.getUserId()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("MEMBER-001"));
    }
}
