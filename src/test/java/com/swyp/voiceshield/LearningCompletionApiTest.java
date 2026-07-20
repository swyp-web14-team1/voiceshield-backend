package com.swyp.voiceshield;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class LearningCompletionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void simulationCompleteRequiresAuthenticatedUser() throws Exception {
        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/simulation-complete"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("AUTH-001"))
                .andExpect(jsonPath("$.error.message").value("로그인이 필요한 기능입니다."));
    }

    @Test
    void simulationCompleteStoresInProgressStatus() throws Exception {
        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/simulation-complete")
                        .header("X-User-Id", "user-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.data.completed").value(false));

        String status = jdbcTemplate.queryForObject(
                "select status from learning_histories where user_id = ? and scenario_id = ?",
                String.class,
                "user-1",
                "case-mobile-repair"
        );
        org.junit.jupiter.api.Assertions.assertEquals("IN_PROGRESS", status);
    }

    @Test
    void quizCompleteMarksScenarioAsComplete() throws Exception {
        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/simulation-complete")
                        .header("X-User-Id", "user-2"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/quiz-complete")
                        .header("X-User-Id", "user-2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.data.status").value("COMPLETE"))
                .andExpect(jsonPath("$.data.completed").value(true));

        String status = jdbcTemplate.queryForObject(
                "select status from learning_histories where user_id = ? and scenario_id = ?",
                String.class,
                "user-2",
                "case-mobile-repair"
        );
        String completedAt = jdbcTemplate.queryForObject(
                "select cast(completed_at as varchar) from learning_histories where user_id = ? and scenario_id = ?",
                String.class,
                "user-2",
                "case-mobile-repair"
        );

        org.junit.jupiter.api.Assertions.assertEquals("COMPLETE", status);
        org.junit.jupiter.api.Assertions.assertNotNull(completedAt);
    }

    @Test
    void simulationCompleteDoesNotDowngradeCompletedScenario() throws Exception {
        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/quiz-complete")
                        .header("X-User-Id", "user-3"))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/learning/scenarios/case-mobile-repair/simulation-complete")
                        .header("X-User-Id", "user-3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("COMPLETE"))
                .andExpect(jsonPath("$.data.completed").value(true))
                .andExpect(jsonPath("$.data.message").value("이미 최종 완료된 학습입니다."));

        String status = jdbcTemplate.queryForObject(
                "select status from learning_histories where user_id = ? and scenario_id = ?",
                String.class,
                "user-3",
                "case-mobile-repair"
        );

        org.junit.jupiter.api.Assertions.assertEquals("COMPLETE", status);
    }
}
