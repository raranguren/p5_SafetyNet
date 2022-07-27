package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.repository.FirestationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FirestationControllerIT {

    private final String ENDPOINT = "/firestation";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FirestationRepository firestationRepository;

    @Test
    public void create_then_update_then_delete() throws Exception {
        // ARRANGE
        var mapper = new ObjectMapper();
        var firestation = new Firestation("123 rue A", "10");
        var postRequest = post(ENDPOINT)
                .content(mapper.writeValueAsString(firestation))
                .contentType(APPLICATION_JSON);
        var deleteRequest = delete(ENDPOINT)
                .content(mapper.writeValueAsString(firestation))
                .contentType(APPLICATION_JSON);
        var modifiedFirestation = new Firestation(firestation.address, "22");
        var modifiedFirestationAsJson = mapper.writeValueAsString(modifiedFirestation);
        var putRequest = put(ENDPOINT)
                .content(modifiedFirestationAsJson)
                .contentType(APPLICATION_JSON);
        // ACT
        var postAction = mockMvc.perform(postRequest);
        var putAction = mockMvc.perform(putRequest);
        var storedStationNumber = firestationRepository.read(firestation.address).get();
        var deleteAction = mockMvc.perform(deleteRequest);
        var searchAfterDeletion = firestationRepository.read(firestation.address);
        // ASSERT
        postAction.andExpect(status().isCreated());
        putAction.andExpect(status().isOk());
        assertEquals(modifiedFirestation.station, storedStationNumber);
        deleteAction.andExpect(status().isOk());
        assert(searchAfterDeletion.isEmpty());
    }

}
