package com.ricaragas.safetynet.unit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.dto.JsonDataSourceDTO;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTest {

    @Mock
    ObjectMapper jsonMapper;

    // sample data
    String addressA = "123 rue";
    String stationA = "1";
    String addressB = "456 av";
    String stationB = "2";
    Firestation firestationA = new Firestation(addressA, stationA);
    Firestation firestationB = new Firestation(addressB, stationB);

    private FirestationRepository repository;

    @BeforeEach
    public void before_each() throws IOException {
        var mockDTO = new JsonDataSourceDTO();
        mockDTO.firestations = new ArrayList<>();
        mockDTO.firestations.add(firestationA);
        when(jsonMapper.readValue(any(URL.class),eq(JsonDataSourceDTO.class))).thenReturn(mockDTO);
        repository = new FirestationRepository(jsonMapper);
    }

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        repository.create(firestation);
        String result = repository.read(firestation.address).get();
        // ASSERT
        assertEquals(firestation.station, result);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        Firestation firestation = firestationA;
        // ACT
        Executable action = () -> repository.create(firestation);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_read_then_value() throws Exception {
        // ARRANGE
        Firestation firestation = firestationA;
        // ACT
        String result = repository.read(firestation.address).get();
        // ASSERT
        assertEquals(firestation.station, result);
    }

    @Test
    public void when_read_then_empty() throws Exception {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        Optional<String> result = repository.read(firestation.address);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_update_then_success() throws Exception {
        // ARRANGE
        String address = firestationA.address;
        String newStation = "3";
        Firestation firestationA_modified = new Firestation(address, newStation);
        // ACT
        repository.update(firestationA_modified);
        Optional<String> result = repository.read(address);
        // ASSERT
        assertEquals(newStation, result.get());
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        Executable action = () -> repository.update(firestation);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_by_address_then_success() throws Exception {
        // ARRANGE
        String address = firestationA.address;
        // ACT
        repository.delete(address);
        Optional<String> result = repository.read(address);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_delete_by_address_then_fail() throws Exception {
        // ARRANGE
        String address = firestationB.address;
        // ACT
        Executable action = () -> repository.delete(address);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_by_station_then_success() throws Exception {
        // ARRANGE
        repository.create(new Firestation(addressB, stationA));
        // ACT
        repository.deleteAllByStation(stationA);
        Optional<String> resultA = repository.read(addressA);
        Optional<String> resultB = repository.read(addressB);
        // ASSERT
        assert(resultA.isEmpty());
        assert(resultB.isEmpty());
    }

    @Test
    public void when_delete_by_station_other_values_remain() throws Exception {
        // ARRANGE
        repository.create(firestationB);
        // ACT
        repository.deleteAllByStation(stationA);
        String result = repository.read(addressB).get();
        // ASSERT
        assertEquals(stationB, result);
    }
}
