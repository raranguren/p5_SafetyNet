package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.dto.FirestationCoveragePerStationDTO;
import com.ricaragas.safetynet.dto.FloodInfoPerAddressDTO;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.FirestationService;
import com.ricaragas.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @InjectMocks
    private FirestationService firestationService;

    @Mock
    private FirestationRepository firestationRepository;

    @Mock
    private PersonService personService;

    // Two objects with the same values to verify that the service calls the repository
    Firestation preparedValue = new Firestation("13 rue Mock","13");
    Firestation expectedValue = new Firestation("13 rue Mock","13");


    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        // ACT
        firestationService.create(preparedValue);
        // ASSERT
        verify(firestationRepository, times(1)).create(expectedValue);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        doThrow(AlreadyExistsException.class).when(firestationRepository).create(any());
        // ACT
        Executable action = () -> firestationService.create(preparedValue);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_update_then_success() throws Exception {
        // ARRANGE
        // ACT
        firestationService.update(preparedValue);
        // ASSERT
        verify(firestationRepository, times(1)).update(expectedValue);
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(firestationRepository).update(any());
        // ACT
        Executable action = () -> firestationService.update(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_then_success() throws Exception {
        // ARRANGE
        // ACT
        firestationService.delete(preparedValue);
        // ASSERT
        verify(firestationRepository, times(1))
                .delete(expectedValue.address);
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(firestationRepository).delete(anyString());
        // ACT
        Executable action = () -> firestationService.delete(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_coverage_by_station_then_success() throws Exception {
        // ARRANGE
        ArrayList<String> addresses = new ArrayList<>(List.of("123abc"));
        ArrayList<Person> persons = new ArrayList<>(List.of(new Person()));
        var report = new FirestationCoveragePerStationDTO();
        when(firestationRepository.getAddressesByStationNumber("1")).thenReturn(addresses);
        when(personService.getPersonsByAddress("123abc")).thenReturn(persons);
        when(personService.getCoverageReportFromPersonList(any())).thenReturn(report);
        // ACT
        var result = firestationService.getCoverageReportByStationNumber("1");
        // ASSERT
        verify(personService, times(1))
                .getPersonsByAddress("123abc");
        verify(personService, times(1))
                .getCoverageReportFromPersonList(any());
    }

    @Test
    public void when_phone_numbers_then_success() throws Exception {
        // ARRANGE
        String stationNumber = "1";
        ArrayList<String> phoneNumbers = new ArrayList<>(List.of("111","222","333"));
        ArrayList<String> addresses = new ArrayList<>(List.of("rue A"));
        when(firestationRepository.getAddressesByStationNumber(any()))
                .thenReturn(addresses);
        when(personService.getAllPhoneNumbersByAddress(anyString()))
                .thenReturn(phoneNumbers);
        // ACT
        var result = firestationService.getUniquePhoneNumbersByStationNumber(stationNumber);
        // ASSERT
        verify(personService, times(1))
                .getAllPhoneNumbersByAddress(addresses.get(0));
        assertEquals(3, result.size());
    }

    @Test
    public void when_phone_numbers_then_unique_values() throws Exception {
        // ARRANGE
        String stationNumber = "1";
        ArrayList<String> phoneNumbers = new ArrayList<>(List.of("111","222","333","333"));
        ArrayList<String> addresses = new ArrayList<>(List.of("rue A", "rue B"));
        when(firestationRepository.getAddressesByStationNumber(any()))
                .thenReturn(addresses);
        when(personService.getAllPhoneNumbersByAddress(anyString()))
                .thenReturn(phoneNumbers);
        // ACT
        var result = firestationService.getUniquePhoneNumbersByStationNumber(stationNumber);
        // ASSERT
        assertEquals(3, result.size());
    }

    @Test
    public void get_fire_alert_by_address_then_success() throws Exception {
        // ARRANGE
        String address = "123";
        when(firestationRepository.read(address)).thenReturn(Optional.of("1"));
        // ACT
        var result = firestationService.getFireAlertByAddress(address);
        // ASSERT
        assertEquals("1", result.get().station);
        verify(firestationRepository, times(1))
                .read(address);
        verify(personService, times(1))
                .getFireAlertsByAddress(address);
    }

    @Test
    public void get_flood_info_by_stations_then_success() throws Exception {
        // ARRANGE
        String station = "1";
        var stations = new ArrayList<>(List.of(station));
        String address = "123";
        when(firestationRepository.getAddressesByStationNumber(station))
                .thenReturn(new ArrayList<>(List.of(address)));
        when(personService.getFloodInfoByAddress(address))
                .thenReturn(Optional.of(new FloodInfoPerAddressDTO()));
        // ACT
        var result = firestationService.getFloodInfoByStationNumbers(stations);
        // ASSERT
        verify(firestationRepository, times(1))
                .getAddressesByStationNumber(station);
        verify(personService, times(1))
                .getFloodInfoByAddress(address);
        assertEquals(1, result.size());
    }

}
