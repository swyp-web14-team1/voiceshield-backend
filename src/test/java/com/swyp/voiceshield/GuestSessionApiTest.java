package com.swyp.voiceshield;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class GuestSessionApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createGuestSessionReturnsGuestIdentifierAndAvailableFeatures() throws Exception {
        mockMvc.perform(post("/api/v1/guest-sessions"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.guestSessionId", notNullValue()))
                .andExpect(jsonPath("$.data.availableFeatures",
                        containsInAnyOrder("CATEGORY_VIEW", "CASE_DETAIL_VIEW", "REPORT_GUIDE_VIEW")));
    }
}
