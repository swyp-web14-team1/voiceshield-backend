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
class LearningOverviewApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void overviewRequiresAuthenticatedUser() throws Exception {
        mockMvc.perform(get("/api/v1/learning/overview"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH-001"))
                .andExpect(jsonPath("$.error.message").value("로그인이 필요한 기능입니다."));
    }

    @Test
    void returnsOverviewWithProgressAndRecentLearning() throws Exception {
        mockMvc.perform(post("/api/v1/learning/scenarios/case-return-delivery/quiz-complete")
                        .header("X-User-Id", "overview-user"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/simulation-complete")
                        .header("X-User-Id", "overview-user"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/learning/overview")
                        .header("X-User-Id", "overview-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.completedScenarioCount").value(1))
                .andExpect(jsonPath("$.data.totalScenarioCount").value(4))
                .andExpect(jsonPath("$.data.progressPercentage").value(25))
                .andExpect(jsonPath("$.data.recentLearning.length()").value(2))
                .andExpect(jsonPath("$.data.recentLearning[0].scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.data.recentLearning[0].title").value("휴대폰 고장"))
                .andExpect(jsonPath("$.data.recentLearning[0].categoryId").value("category-family-impersonation"))
                .andExpect(jsonPath("$.data.recentLearning[0].categoryName").value("가족 사칭"))
                .andExpect(jsonPath("$.data.recentLearning[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.data.recentLearning[0].lastLearnedAt").isNotEmpty())
                .andExpect(jsonPath("$.data.recentLearning[1].scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.recentLearning[1].title").value("반품 택배"))
                .andExpect(jsonPath("$.data.recentLearning[1].categoryId").value("category-delivery-impersonation"))
                .andExpect(jsonPath("$.data.recentLearning[1].categoryName").value("택배 사칭"))
                .andExpect(jsonPath("$.data.recentLearning[1].status").value("COMPLETE"))
                .andExpect(jsonPath("$.data.recentLearning[1].lastLearnedAt").isNotEmpty());
    }
}
