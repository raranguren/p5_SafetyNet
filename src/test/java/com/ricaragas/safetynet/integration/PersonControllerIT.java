package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.PersonRepository;
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
public class PersonControllerIT {

    private final String ENDPOINT = "/person";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void create_then_update_then_delete() throws Exception {
        // ARRANGE
        var mapper = new ObjectMapper();
        var person = new Person("Abcd", "Efgh",
                "", "", "", "", "");
        var postRequest = post(ENDPOINT)
                .content(mapper.writeValueAsString(person))
                .contentType(APPLICATION_JSON);
        var deleteRequest = delete(ENDPOINT)
                .content(mapper.writeValueAsString(person))
                .contentType(APPLICATION_JSON);
        var modifiedPerson = new Person(person.firstName, person.lastName,
                "123 rue", "Paris", "123", "111-111", "abc@mail.com");
        var modifiedPersonAsJson = mapper.writeValueAsString(modifiedPerson);
        var putRequest = put(ENDPOINT)
                .content(modifiedPersonAsJson)
                .contentType(APPLICATION_JSON);
        // ACT
        var postAction = mockMvc.perform(postRequest);
        var putAction = mockMvc.perform(putRequest);
        var storedPerson = personRepository.read(person.firstName, person.lastName).get();
        var storedPersonAsJson = mapper.writeValueAsString(storedPerson);
        var deleteAction = mockMvc.perform(deleteRequest);
        var searchAfterDeletion = personRepository.read(person.firstName, person.lastName);
        // ASSERT
        postAction.andExpect(status().isCreated());
        putAction.andExpect(status().isOk());
        assertEquals(modifiedPersonAsJson, storedPersonAsJson);
        deleteAction.andExpect(status().isOk());
        assert(searchAfterDeletion.isEmpty());
    }
}
