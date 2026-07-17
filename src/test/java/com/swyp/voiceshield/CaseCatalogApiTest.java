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
                .andExpect(jsonPath("$.scenarioId").value("case-return-delivery"))
                .andExpect(jsonPath("$.caseName").value("반품 택배"))
                .andExpect(jsonPath("$.variants.message.channel").value("MESSAGE"))
                .andExpect(jsonPath("$.variants.voice.channel").value("VOICE"));
    }

    @Test
    void keepsMobileRepairAsItsOwnCategory() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caseName").value("휴대폰 고장"))
                .andExpect(jsonPath("$.categoryId").doesNotExist())
                .andExpect(jsonPath("$.categoryName").doesNotExist());
    }

    @Test
    void returnsVoiceScriptAndQuizOptionsForMobileRepair() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.variants.voice.content").value(containsString("휴대폰이 고장 나서")))
                .andExpect(jsonPath("$.variants.voice.options", hasSize(4)))
                .andExpect(jsonPath("$.variants.voice.options[1].optionNumber").value(2))
                .andExpect(jsonPath("$.variants.voice.options[1].isCorrect").value(true))
                .andExpect(jsonPath("$.variants.voice.options[3].isCorrect").value(true));
    }

    @Test
    void returnsVoiceScenarioStepWithScriptLinesAndChoices() throws Exception {
        mockMvc.perform(get("/api/v1/cases/case-mobile-repair/variants/voice/scenario-step"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.scenarioId").value("case-mobile-repair"))
                .andExpect(jsonPath("$.variantId").value("case-mobile-repair-voice"))
                .andExpect(jsonPath("$.channel").value("VOICE"))
                .andExpect(jsonPath("$.scriptLines", hasSize(23)))
                .andExpect(jsonPath("$.scriptLines[0]").value("[전화벨]"))
                .andExpect(jsonPath("$.scriptLines[1]").value("나"))
                .andExpect(jsonPath("$.scriptLines[2]").value("\"여보세요?\""))
                .andExpect(jsonPath("$.choices", hasSize(4)))
                .andExpect(jsonPath("$.choices[1].optionText").value("기존에 저장된 아들 번호로 직접 전화한다."))
                .andExpect(jsonPath("$.choices[1].isCorrect").value(true))
                .andExpect(jsonPath("$.choices[3].isCorrect").value(true));
    }

    @Test
    void evaluatesSelectedVoiceScenarioChoice() throws Exception {
        mockMvc.perform(post("/api/v1/cases/case-mobile-repair/variants/voice/choices")
                        .contentType("application/json")
                        .content("{\"choiceOptionId\":\"case-mobile-repair-voice-option-2\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.choiceOptionId").value("case-mobile-repair-voice-option-2"))
                .andExpect(jsonPath("$.optionNumber").value(2))
                .andExpect(jsonPath("$.isCorrect").value(true));
    }
}
