package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CaseCatalogApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsMessageAndVoiceVariantsForScenario() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-return-delivery"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.caseName").value("반품 택배"))
                .andExpect(jsonPath("$.data.variants.message.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.data.variants.voice.channel").value("VOICE"));
    }

    @Test
    void keepsMobileRepairAsItsOwnCategory() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.caseName").value("휴대폰 고장"))
                .andExpect(jsonPath("$.data.categoryId").doesNotExist())
                .andExpect(jsonPath("$.data.categoryName").doesNotExist());
    }

    @Test
    void returnsVoiceScriptAndQuizOptionsForMobileRepair() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.variants.voice.content").value(containsString("휴대폰이 고장 나서")))
                .andExpect(jsonPath("$.data.variants.voice.options", hasSize(4)))
                .andExpect(jsonPath("$.data.variants.voice.options[1].optionNumber").value(2))
                .andExpect(jsonPath("$.data.variants.voice.options[1].isCorrect").value(true))
                .andExpect(jsonPath("$.data.variants.voice.options[3].isCorrect").value(true));
    }

    @Test
    void returnsVoiceScenarioStepWithScriptLinesAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair/variants/voice/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.data.variantId").value("case-mobile-repair-voice"))
                .andExpect(jsonPath("$.data.channel").value("VOICE"))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-mobile-repair-voice-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.quizNumber").value(1))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기임을 판단할 수 있는 결정적인 단서는 무엇일까요?"))
                .andExpect(jsonPath("$.data.scriptLines", hasSize(23)))
                .andExpect(jsonPath("$.data.scriptLines[0]").value("[전화벨]"))
                .andExpect(jsonPath("$.data.scriptLines[1]").value("나"))
                .andExpect(jsonPath("$.data.scriptLines[2]").value("\"여보세요?\""))
                .andExpect(jsonPath("$.data.choices", hasSize(4)))
                .andExpect(jsonPath("$.data.choices[1].optionText").value("기존에 저장된 아들 번호로 직접 전화한다."))
                .andExpect(jsonPath("$.data.choices[1].isCorrect").value(true))
                .andExpect(jsonPath("$.data.choices[3].isCorrect").value(true));
    }

    @Test
    void evaluatesSelectedVoiceScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-mobile-repair/variants/voice/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-mobile-repair-voice-option-2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.choiceOptionId").value("case-mobile-repair-voice-option-2"))
                .andExpect(jsonPath("$.data.optionNumber").value(2))
                .andExpect(jsonPath("$.data.isCorrect").value(true))
                .andExpect(jsonPath("$.data.quiz.quizId").value("case-mobile-repair-voice-quiz-1"))
                .andExpect(jsonPath("$.data.quiz.question").value("다음 중 사기임을 판단할 수 있는 결정적인 단서는 무엇일까요?"))
                .andExpect(jsonPath("$.data.selectedOption.optionId").value("case-mobile-repair-voice-option-2"))
                .andExpect(jsonPath("$.data.correctOption.optionId").value("case-mobile-repair-voice-option-2"))
                .andExpect(jsonPath("$.data.explanation").value("낯선 사람이 알려준 연락처나 계좌를 그대로 이용하지 말고, 공식 대표번호나 기존에 저장된 번호로 직접 사실 여부를 확인해야 합니다."))
                .andExpect(jsonPath("$.data.recommendedLearning.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.data.recommendedLearning.title").value("諛섑뭹 ?앸같"));
    }

    @Test
    void returnsNotFoundForUnknownApi() throws Exception {
        mockMvc.perform(get("/api/v1/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON-002"))
                .andExpect(jsonPath("$.error.message").value("요청한 API를 찾을 수 없습니다."));
    }

    @Test
    void returnsMethodNotAllowedForUnsupportedHttpMethod() throws Exception {
        mockMvc.perform(post("/api/v1/categories"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("COMMON-004"))
                .andExpect(jsonPath("$.error.message").value("지원하지 않는 HTTP 메서드입니다."));
    }
}
