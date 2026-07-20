package com.swyp.voiceshield;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ReportGuideApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returnsReportGuideWithPreventionTips() throws Exception {
        mockMvc.perform(get("/api/v1/report-guide"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.reportSteps", hasSize(3)))
                .andExpect(jsonPath("$.data.emergencyContacts", hasSize(2)))
                .andExpect(jsonPath("$.data.emergencyContacts[0].name").value("경찰청"))
                .andExpect(jsonPath("$.data.emergencyContacts[0].phoneNumber").value("112"))
                .andExpect(jsonPath("$.data.realActionGuide", hasSize(2)))
                .andExpect(jsonPath("$.data.preventionTips", hasSize(3)));
    }
}
