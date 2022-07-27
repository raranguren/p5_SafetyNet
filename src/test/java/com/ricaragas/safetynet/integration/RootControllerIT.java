package com.ricaragas.safetynet.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RootControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void firestation() throws Exception {
        // ARRANGE
        String stationNumber = "1";
        // ACT
        mockMvc.perform(get("/firestation")
                .param("stationNumber", stationNumber))
            // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.childrenCount").isNumber())
                .andExpect(jsonPath("$.adultsCount").isNumber())
                .andExpect(jsonPath("$.coveredPersons").isArray());
    }

}
