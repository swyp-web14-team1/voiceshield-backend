package com.swyp.voiceshield;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LearningProgressApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void progressRequiresAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/v1/learning/progress"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH-001"))
                .andExpect(jsonPath("$.error.message").value("로그인이 필요한 기능입니다."));
    }

    @Test
    void returnsProgressBasedOnCompletedScenarios() throws Exception {
        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/simulation-complete")
                        .header("X-User-Id", "progress-user"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/learning/scenarios/case-return-delivery/quiz-complete")
                        .header("X-User-Id", "progress-user"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/learning/progress")
                        .header("X-User-Id", "progress-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.completedScenarioCount").value(1))
                .andExpect(jsonPath("$.data.totalScenarioCount").value(4))
                .andExpect(jsonPath("$.data.progressPercentage").value(25));
    }
}
