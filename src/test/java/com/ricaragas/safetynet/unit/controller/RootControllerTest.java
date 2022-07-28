package com.ricaragas.safetynet.unit.controller;

import com.ricaragas.safetynet.controller.RootController;
import com.ricaragas.safetynet.dto.ChildAlertPerChildDTO;
import com.ricaragas.safetynet.dto.FirestationCoveragePerStationDTO;
import com.ricaragas.safetynet.service.FirestationService;
import com.ricaragas.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = RootController.class)
public class RootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private FirestationService firestationService;

    @InjectMocks
    private RootController rootController;

    @Test
    public void get_firestation_coverage_with_param_then_success() throws Exception {
        // ARRANGE
        String stationNumber = "1";
        var response = new FirestationCoveragePerStationDTO();
        when(firestationService.getCoverageReportByStationNumber(anyString())).thenReturn(response);
        // ACT
        mockMvc.perform(get("/firestation")
                .param("stationNumber", stationNumber))
                // ASSERT
                .andExpect(status().isOk());
    }

    @Test
    public void get_firestation_coverage_with_no_param_then_fail() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/firestation"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_child_alert_with_param_then_success() throws Exception {
        // ARRANGE
        String address = "123 Rue st";
        var response = new ArrayList<>(List.of(
                new ChildAlertPerChildDTO(),
                new ChildAlertPerChildDTO()));
        when(personService.getChildAlertsByAddress(anyString())).thenReturn(response);
        // ACT
        mockMvc.perform(get("/childAlert")
                .param("address", address))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)));
    }

    @Test
    public void get_child_alert_with_no_param_then_fail() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/childAlert"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

}
