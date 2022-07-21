package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.repository.JsonDataSourceRepository;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonDataSourceRepositoryIT {

    @Test
    public void constructor_reads_json() {
        // ARRANGE
        ObjectMapper jsonMapper = new ObjectMapper();
        // ACT
        var testedRepository = new JsonDataSourceRepository(jsonMapper);
        var data = testedRepository.getData();
        // ASSERT
        assertNotNull(data);
        assertEquals(23, data.persons.length);
        assertEquals(13, data.firestations.length);
        assertEquals(23, data.medicalrecords.length);
    }
}
