package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.FirestationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @InjectMocks
    private FirestationService service;

    @Mock
    private FirestationRepository repository;

    // Two objects with the same values to verify that the service calls the repository
    Firestation preparedValue = new Firestation("13 rue Mock","13");
    Firestation expectedValue = new Firestation("13 rue Mock","13");


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
                .delete(expectedValue.address);
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(repository).delete(anyString());
        // ACT
        Executable action = () -> service.delete(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }


}
