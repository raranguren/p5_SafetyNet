package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @InjectMocks
    private MedicalRecordService service;

    @Mock
    private MedicalRecordRepository repository;

    // Two objects with the same values to verify that the service calls the repository
    MedicalRecord preparedValue = new MedicalRecord("AAA","BBB","01/01/2001",new ArrayList<>(), new ArrayList<>());
    MedicalRecord expectedValue = new MedicalRecord("AAA","BBB","01/01/2001",new ArrayList<>(), new ArrayList<>());
    LocalDate expectedBirthdate = LocalDate.of(2001,1,1);

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        // ACT
        service.create(preparedValue);
        // ASSERT
        verify(repository, times(1)).create(expectedValue);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        doThrow(AlreadyExistsException.class).when(repository).create(any());
        // ACT
        Executable action = () -> service.create(preparedValue);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_update_then_success() throws Exception {
        // ARRANGE
        // ACT
        service.update(preparedValue);
        // ASSERT
        verify(repository, times(1)).update(expectedValue);
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(repository).update(any());
        // ACT
        Executable action = () -> service.update(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_then_success() throws Exception {
        // ARRANGE
        // ACT
        service.delete(preparedValue);
        // ASSERT
        verify(repository, times(1))
                .delete(expectedValue.firstName, expectedValue.lastName);
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(repository).delete(anyString(), anyString());
        // ACT
        Executable action = () -> service.delete(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_get_birthdate_then_valid_date() throws Exception {
        when(repository.read(anyString(), anyString())).thenReturn(Optional.of(expectedValue));
        // ARRANGE
        var result = service.getBirthdateByName(preparedValue.firstName, preparedValue.lastName);
        // ASSERT
        verify(repository, times(1))
                .read(expectedValue.firstName, expectedValue.lastName);
        assertEquals(expectedBirthdate, result.get());
    }

    @Test
    public void when_get_birthdate_then_empty() throws Exception {
        // ARRANGE
        when(repository.read(anyString(), anyString())).thenReturn(Optional.empty());
        // ACT
        var result = service.getBirthdateByName(preparedValue.firstName, preparedValue.lastName);
        // ASSERT
        assert(result.isEmpty());
    }

}
