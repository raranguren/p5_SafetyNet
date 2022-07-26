package com.ricaragas.safetynet.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.repository.JsonDataSourceRepository;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

public class JsonDataSourceRepositoryIT {

    JsonDataSourceRepository repositoryUnderTest;

    @Test
    public void can_connect_to_data_resource() throws IOException {
        // ARRANGE
        String address = JsonDataSourceRepository.DATA_URL;
        URL url = new URL(address);
        InetAddress inetAddress = InetAddress.getByName(url.getHost());
        // ACT
        boolean isReachable = inetAddress.isReachable(60);
        // ASSERT
        assertTrue(isReachable);
    }

    @Test
    public void constructor_reads_json() {
        // ARRANGE
        ObjectMapper jsonMapper = new ObjectMapper();
        // ACT
        repositoryUnderTest = new JsonDataSourceRepository(jsonMapper);
        var actualData = repositoryUnderTest.getData();
        // ASSERT
        assertNotNull(actualData);
        assertEquals(23, actualData.persons.size());
        assertEquals(13, actualData.firestations.size());
        assertEquals(23, actualData.medicalrecords.size());
    }
}
