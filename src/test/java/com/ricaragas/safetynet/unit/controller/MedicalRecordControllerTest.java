package com.ricaragas.safetynet.unit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.controller.MedicalRecordController;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MedicalRecordController.class)
public class MedicalRecordControllerTest {

    private static final String ENDPOINT = "/medicalRecord";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    // example requests
    private static MockHttpServletRequestBuilder getRequest;
    private static MockHttpServletRequestBuilder postRequest;
    private static MockHttpServletRequestBuilder putRequest;
    private static MockHttpServletRequestBuilder deleteRequest;

    @BeforeAll
    static void before_all() throws JsonProcessingException {
        MedicalRecord record = new MedicalRecord("Bea", "Bezos","12/05/1987",
                new ArrayList<>(List.of("terazine:500mg")),
                new ArrayList<>(List.of("peanut", "tradox")));
        String jsonBody = new ObjectMapper().writeValueAsString(record);
        getRequest = get(ENDPOINT);
        postRequest = post(ENDPOINT)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON);
        putRequest = put(ENDPOINT)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON);
        deleteRequest = delete(ENDPOINT)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void when_read_then_not_allowed() throws Exception {
        mockMvc.perform(getRequest)
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void when_create_then_is_created() throws Exception {
        mockMvc.perform(postRequest)
                .andExpect(status().isCreated());
    }

    @Test
    public void when_create_duplicate_then_conflict() throws Exception {
        doThrow(AlreadyExistsException.class).when(medicalRecordService).create(any());
        mockMvc.perform(postRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void when_update_then_ok() throws Exception {
        mockMvc.perform(putRequest)
                .andExpect(status().isOk());
    }

    @Test
    public void when_update_then_conflict() throws Exception {
        doThrow(NotFoundException.class).when(medicalRecordService).update(any());
        mockMvc.perform(putRequest)
                .andExpect(status().isConflict());
    }

    @Test
    public void when_delete_then_ok() throws Exception {
        mockMvc.perform(deleteRequest)
                .andExpect(status().isOk());
    }

    @Test public void when_delete_then_conflict() throws Exception {
        doThrow(NotFoundException.class).when(medicalRecordService).delete(any());
        mockMvc.perform(deleteRequest)
                .andExpect(status().isConflict());
    }


}
