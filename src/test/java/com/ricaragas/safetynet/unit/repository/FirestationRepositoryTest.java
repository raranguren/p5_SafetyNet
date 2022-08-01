package com.ricaragas.safetynet.unit.repository;

import com.ricaragas.safetynet.dto.JsonDataDTO;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.repository.JsonDataRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FirestationRepositoryTest {

    @Mock
    JsonDataRepository jsonDataRepository;

    // sample data
    String addressA = "123 rue";
    String stationA = "1";
    String addressB = "456 av";
    String stationB = "2";
    Firestation firestationA = new Firestation(addressA, stationA);
    Firestation firestationB = new Firestation(addressB, stationB);

    private FirestationRepository firestationRepository;

    @BeforeEach
    public void before_each() throws IOException {
        var mockDTO = new JsonDataDTO();
        mockDTO.firestations = new ArrayList<>();
        mockDTO.firestations.add(firestationA);
        when(jsonDataRepository.get()).thenReturn(mockDTO);
        firestationRepository = new FirestationRepository(jsonDataRepository);
    }

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        firestationRepository.create(firestation);
        String result = firestationRepository.read(firestation.address).get();
        // ASSERT
        assertEquals(firestation.station, result);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        Firestation firestation = firestationA;
        // ACT
        Executable action = () -> firestationRepository.create(firestation);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_read_then_value() throws Exception {
        // ARRANGE
        Firestation firestation = firestationA;
        // ACT
        String result = firestationRepository.read(firestation.address).get();
        // ASSERT
        assertEquals(firestation.station, result);
    }

    @Test
    public void when_read_then_empty() throws Exception {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        Optional<String> result = firestationRepository.read(firestation.address);
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
        firestationRepository.update(firestationA_modified);
        Optional<String> result = firestationRepository.read(address);
        // ASSERT
        assertEquals(newStation, result.get());
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        Executable action = () -> firestationRepository.update(firestation);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_by_address_then_success() throws Exception {
        // ARRANGE
        String address = firestationA.address;
        // ACT
        firestationRepository.delete(address);
        Optional<String> result = firestationRepository.read(address);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_delete_by_address_then_fail() throws Exception {
        // ARRANGE
        String address = firestationB.address;
        // ACT
        Executable action = () -> firestationRepository.delete(address);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_by_station_then_success() throws Exception {
        // ARRANGE
        firestationRepository.create(new Firestation(addressB, stationA));
        // ACT
        firestationRepository.deleteAllByStation(stationA);
        Optional<String> resultA = firestationRepository.read(addressA);
        Optional<String> resultB = firestationRepository.read(addressB);
        // ASSERT
        assert(resultA.isEmpty());
        assert(resultB.isEmpty());
    }

    @Test
    public void when_delete_by_station_other_values_remain() throws Exception {
        // ARRANGE
        firestationRepository.create(firestationB);
        // ACT
        firestationRepository.deleteAllByStation(stationA);
        String result = firestationRepository.read(addressB).get();
        // ASSERT
        assertEquals(stationB, result);
    }

    @Test
    public void when_read_by_station_number_then_value() {
        // ARRANGE
        Firestation firestation = firestationA;
        // ACT
        var result = firestationRepository.getAddressesByStationNumber(firestation.station);
        // ASSERT
        assertEquals(1, result.size());
    }

    @Test
    public void when_read_by_station_number_then_empty() {
        // ARRANGE
        Firestation firestation = firestationB;
        // ACT
        var result = firestationRepository.getAddressesByStationNumber(firestation.station);
        // ASSERT
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
