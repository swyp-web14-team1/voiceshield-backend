package com.swyp.voiceshield;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swyp.voiceshield.guest.GuestSessionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GuestSessionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GuestSessionRepository guestSessionRepository;

    @Test
    void createGuestSessionPersistsGuestIdentifierAndReturnsAvailableFeatures() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/guest-sessions"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.guestSessionId", notNullValue()))
                .andExpect(jsonPath("$.data.availableFeatures",
                        containsInAnyOrder("CATEGORY_VIEW", "CASE_DETAIL_VIEW", "REPORT_GUIDE_VIEW")))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode responseJson = objectMapper.readTree(responseBody);
        String guestSessionId = responseJson.path("data").path("guestSessionId").asText();

        assertThat(guestSessionRepository.existsById(guestSessionId)).isTrue();
    }
}
