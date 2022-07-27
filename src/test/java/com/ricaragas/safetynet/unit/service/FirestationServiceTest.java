package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.dto.FirestationCoveragePerPersonDTO;
import com.ricaragas.safetynet.dto.FirestationCoveragePerStationDTO;
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
    private FirestationRepository repository;

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
        verify(repository, times(1)).create(expectedValue);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        doThrow(AlreadyExistsException.class).when(repository).create(any());
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
        verify(repository, times(1)).update(expectedValue);
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(repository).update(any());
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
        verify(repository, times(1))
                .delete(expectedValue.address);
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(repository).delete(anyString());
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
        when(repository.getAddressesByStationNumber("1")).thenReturn(addresses);
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

}
