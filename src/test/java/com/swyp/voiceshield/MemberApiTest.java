package com.swyp.voiceshield;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
}
