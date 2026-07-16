package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;

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
}
