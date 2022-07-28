package com.ricaragas.safetynet.unit.controller;

import com.ricaragas.safetynet.controller.RootController;
import com.ricaragas.safetynet.dto.ChildAlertPerChildDTO;
import com.ricaragas.safetynet.dto.FireAlertPerAddressDTO;
import com.ricaragas.safetynet.dto.FirestationCoveragePerStationDTO;
import com.ricaragas.safetynet.dto.PersonInfoPerPersonDTO;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


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
        when(firestationService.getCoverageReportByStationNumber(anyString()))
                .thenReturn(Optional.of(response));
        // ACT
        mockMvc.perform(get("/firestation")
                .param("stationNumber", stationNumber))
                // ASSERT
                .andExpect(status().isOk());
    }

    @Test
    public void get_firestation_coverage_and_station_not_found_then_empty_json() throws Exception {
        // ARRANGE
        when(firestationService.getCoverageReportByStationNumber(anyString()))
                .thenReturn(Optional.empty());
        // ACT
        mockMvc.perform(get("/firestation")
                .param("stationNumber", anyString()))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
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
    public void get_child_alert_when_address_not_found_then_empty_list() throws Exception {
        // ARRANGE
        when(personService.getChildAlertsByAddress(anyString())).thenReturn(new ArrayList<>());
        // ACT
        mockMvc.perform(get("/childAlert")
                .param("address", "XXX"))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void get_child_alert_with_no_param_then_fail() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/childAlert"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_phone_alert_with_param_then_success() throws Exception {
        // ARRANGE
        when(firestationService.getUniquePhoneNumbersByStationNumber(anyString()))
                .thenReturn(new ArrayList<>(List.of("111","222","333")));
        // ACT
        mockMvc.perform(get("/phoneAlert")
                .param("firestation", anyString()))
                //ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)));
    }

    @Test
    public void get_phone_alert_when_station_not_found_then_empty_list() throws Exception {
        // ARRANGE
        when(firestationService.getUniquePhoneNumbersByStationNumber(anyString()))
                .thenReturn((new ArrayList<>()));
        // ACT
        mockMvc.perform(get("/phoneAlert")
                        .param("firestation", anyString()))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void get_phone_alert_with_no_param_then_fail() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/phoneAlert"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_fire_with_param_then_success() throws Exception {
        // ARRANGE
        when(firestationService.getFireAlertByAddress(anyString()))
                .thenReturn(Optional.of(new FireAlertPerAddressDTO()));
        // ACT
        mockMvc.perform(get("/fire")
                        .param("address", anyString()))
                //ASSERT
                .andExpect(status().isOk());
    }

    @Test
    public void get_fire_when_station_does_not_exist_then_empty() throws Exception {
        // ARRANGE
        when(firestationService.getFireAlertByAddress(anyString()))
                .thenReturn(Optional.empty());
        // ACT
        mockMvc.perform(get("/fire")
                        .param("address", anyString()))
                //ASSERT
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    public void get_fire_with_no_param_then_fail() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/fire"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_flood_with_params_then_success() throws Exception {
        // ARRANGE
        when(firestationService.getFloodInfoByStationNumbers(any()))
                .thenReturn(new ArrayList<>());
        // ACT
        mockMvc.perform(get("/flood/stations")
                .param("stations", "1, 2"))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }

    @Test
    public void get_flood_with_no_param_then_fail() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/flood/stations"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_person_info_with_params_then_success() throws Exception {
        // ARRANGE
        String firstName = "AAA";
        String lastName = "BBB";
        var personInfo = new PersonInfoPerPersonDTO();
        when(personService.getPersonInfo(eq(firstName), eq(lastName)))
                .thenReturn(Optional.of(personInfo));
        // ACT
        mockMvc.perform(get("/personInfo")
                .param("firstName", firstName)
                .param("lastName", lastName))
                // ASSERT
                .andExpect(status().isOk());
    }

    @Test
    public void get_person_info_and_person_not_found_then_bad_request() throws Exception {
        // ARRANGE
        when(personService.getPersonInfo(any(), any()))
                .thenReturn(Optional.empty());
        // ACT
        mockMvc.perform(get("/personInfo")
                        .param("firstName", anyString())
                        .param("lastName", anyString()))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_person_info_with_no_params_then_bad_request() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/personInfo"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

    @Test
    public void get_community_email_with_param_then_success() throws Exception {
        // ARRANGE
        when(personService.getEmailsByCity(anyString()))
                .thenReturn(new ArrayList<>(List.of("abc@abc.com")));
        // ACT
        mockMvc.perform(get("/communityEmail")
                .param("city", anyString()))
                // ASSERT
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").value("abc@abc.com"));

    }

    @Test
    public void get_community_email_with_no_params_then_bad_request() throws Exception {
        // ARRANGE
        // ACT
        mockMvc.perform(get("/communityEmail"))
                // ASSERT
                .andExpect(status().isBadRequest());
    }

}
