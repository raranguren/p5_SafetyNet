package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.repository.JsonDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JsonDataRepositoryIT {

    @Autowired
    JsonDataRepository jsonDataRepository;

    @Test
    public void reads_json() {
        // ARRANGE
        // ACT
        var data = jsonDataRepository.get();
        // ASSERT
        assertNotNull(data);
        assertEquals(23, data.persons.size());
        assertEquals(13, data.firestations.size());
        assertEquals(23, data.medicalrecords.size());
    }
}
