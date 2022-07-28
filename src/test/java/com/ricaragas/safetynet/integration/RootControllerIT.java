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

    @Test
    public void childAlert() throws Exception {
        // ARRANGE
        String address = "1509 Culver St";
        // ACT
        mockMvc.perform(get("/childAlert")
                .param("address", address))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$[0].firstName").isString())
                .andExpect(jsonPath("$[0].age").isNumber())
                .andExpect(jsonPath("$[0].otherResidents").isArray())
                .andExpect(jsonPath("$[0].otherResidents[0].firstName").isString());
    }

    @Test
    public void phoneAlert() throws Exception {
        // ARRANGE
        String station = "1";
        // ACT
        mockMvc.perform(get("/phoneAlert")
                .param("firestation", station))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$[0]").isString());
    }

    @Test
    public void fire() throws Exception {
        // ARRANGE
        String address = "1509 Culver St";
        // ACT
        mockMvc.perform(get("/fire")
                .param("address", address))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.station").isString())
                .andExpect(jsonPath("$.habitants").isArray())
                .andExpect(jsonPath("$.habitants[0].lastName").isString());
    }

    @Test
    public void flood_stations() throws Exception {
        // ARRANGE
        String stations = "1, 2";
        // ACT
        mockMvc.perform(get("/flood/stations")
                .param("stations",stations))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*").isArray())
                .andExpect(jsonPath("$[0].address").isString())
                .andExpect(jsonPath("$[0].persons").isArray())
                .andExpect(jsonPath("$[0].persons[0].age").isNumber());
    }

    @Test
    public void personInfo() throws Exception {
        // ARRANGE
        String firstName = "Jacob";
        String lastName = "Boyd";
        // ACT
        mockMvc.perform(get("/personInfo")
                .param("firstName", firstName)
                .param("lastName", lastName))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").isString())
                .andExpect(jsonPath("$.allergies").isArray());
    }
}
