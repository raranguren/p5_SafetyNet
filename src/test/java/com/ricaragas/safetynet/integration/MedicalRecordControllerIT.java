package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MedicalRecordControllerIT {

    private final String ENDPOINT = "/medicalRecord";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Test
    public void create_then_update_then_delete() throws Exception {
        // ARRANGE
        var mapper = new ObjectMapper();
        var record = new MedicalRecord(
                "Abdd", "Efgh",
                "31/01/2001",
                new ArrayList<>(),
                new ArrayList<>());
        var postRequest = post(ENDPOINT)
                .content(mapper.writeValueAsString(record))
                .contentType(APPLICATION_JSON);
        var deleteRequest = delete(ENDPOINT)
                .content(mapper.writeValueAsString(record))
                .contentType(APPLICATION_JSON);
        var modifiedRecord = new MedicalRecord(
                record.firstName, record.lastName,
                "01/02/2012",
                new ArrayList<>(List.of("Zzz:2")),
                new ArrayList<>(List.of("AB", "CD", "EF")));
        var modifiedRecordAsJson = mapper.writeValueAsString(modifiedRecord);
        var putRequest = put(ENDPOINT)
                .content(modifiedRecordAsJson)
                .contentType(APPLICATION_JSON);
        // ACT
        var postAction = mockMvc.perform(postRequest);
        var putAction = mockMvc.perform(putRequest);
        var storedRecord = medicalRecordRepository.read(record.firstName, record.lastName).get();
        var storedRecordAsJson = mapper.writeValueAsString(storedRecord);
        var deleteAction = mockMvc.perform(deleteRequest);
        var searchAfterDeletion = medicalRecordRepository.read(record.firstName, record.lastName);
        // ASSERT
        postAction.andExpect(status().isCreated());
        putAction.andExpect(status().isOk());
        assertEquals(modifiedRecordAsJson, storedRecordAsJson);
        deleteAction.andExpect(status().isOk());
        assert(searchAfterDeletion.isEmpty());
    }

}
