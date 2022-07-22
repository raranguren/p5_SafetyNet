package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.repository.JsonDataSourceRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonDataSourceRepositoryIT {

    JsonDataSourceRepository repositoryUnderTest;

    @Test
    public void constructor_reads_json() {
        // ARRANGE
        ObjectMapper jsonMapper = new ObjectMapper();
        // ACT
        repositoryUnderTest = new JsonDataSourceRepository(jsonMapper);
        var actualData = repositoryUnderTest.getData();
        // ASSERT
        assertNotNull(actualData);
        assertEquals(23, actualData.persons.length);
        assertEquals(13, actualData.firestations.length);
        assertEquals(23, actualData.medicalrecords.length);
    }
}
