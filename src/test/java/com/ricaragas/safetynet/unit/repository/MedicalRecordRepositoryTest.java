package com.ricaragas.safetynet.unit.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.dto.JsonDataSourceDTO;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
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
public class MedicalRecordRepositoryTest {

    @Mock
    ObjectMapper jsonMapper;

    // sample data
    ArrayList<String> medications = new ArrayList<>();
    ArrayList<String> allergies = new ArrayList<>();
    MedicalRecord recordA = new MedicalRecord("Ada", "Ab", "11/1/2011", medications, allergies);
    MedicalRecord recordB = new MedicalRecord("Bea", "Bec", "22/2/2022", medications, allergies);

    private MedicalRecordRepository repository;

    @BeforeEach
    public void before_each() throws IOException {
        var mockDTO = new JsonDataSourceDTO();
        mockDTO.medicalrecords = new ArrayList<>();
        mockDTO.medicalrecords.add(recordA);
        when(jsonMapper.readValue(any(URL.class),eq(JsonDataSourceDTO.class))).thenReturn(mockDTO);
        repository = new MedicalRecordRepository(jsonMapper);
    }

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        MedicalRecord record = recordB;
        // ACT
        repository.create(record);
        MedicalRecord result = repository.read(record.firstName, record.lastName).get();
        // ASSERT
        assertEquals(record, result);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        MedicalRecord record = recordA;
        // ACT
        Executable action = () -> repository.create(record);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_read_then_value() throws Exception {
        // ARRANGE
        String firstName = recordA.firstName;
        String lastName = recordA.lastName;
        // ACT
        MedicalRecord result = repository.read(firstName,lastName).get();
        // ASSERT
        assertEquals(recordA, result);
    }

    @Test
    public void when_read_then_empty() throws Exception {
        // ARRANGE
        String firstName = recordB.firstName;
        String lastName = recordB.lastName;
        // ACT
        Optional<MedicalRecord> result = repository.read(firstName, lastName);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_update_then_success() throws Exception {
        // ARRANGE
        String firstName = recordA.firstName;
        String lastName = recordA.lastName;
        MedicalRecord recordA_modified = new MedicalRecord(firstName, lastName,
                "9/9/2019",medications,allergies);
        // ACT
        repository.update(recordA_modified);
        Optional<MedicalRecord> result = repository.read(firstName, lastName);
        // ASSERT
        assertEquals(recordA_modified, result.get());
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        MedicalRecord record = recordB;
        // ACT
        Executable action = () -> repository.update(record);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_then_success() throws Exception {
        // ARRANGE
        String firstName = recordA.firstName;
        String lastName = recordA.lastName;
        // ACT
        repository.delete(firstName, lastName);
        Optional<MedicalRecord> result = repository.read(firstName, lastName);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        String firstName = recordB.firstName;
        String lastName = recordB.lastName;
        // ACT
        Executable action = () -> repository.delete(firstName, lastName);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

}
