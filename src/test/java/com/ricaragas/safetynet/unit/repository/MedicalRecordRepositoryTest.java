package com.ricaragas.safetynet.unit.repository;

import com.ricaragas.safetynet.dto.JsonDataDTO;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.JsonDataRepository;
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
    JsonDataRepository jsonDataRepository;

    // sample data
    ArrayList<String> medications = new ArrayList<>();
    ArrayList<String> allergies = new ArrayList<>();
    MedicalRecord recordA = new MedicalRecord("Ada", "Ab", "11/1/2011", medications, allergies);
    MedicalRecord recordB = new MedicalRecord("Bea", "Bec", "22/2/2022", medications, allergies);

    private MedicalRecordRepository medicalRecordRepository;

    @BeforeEach
    public void before_each() throws IOException {
        var mockDTO = new JsonDataDTO();
        mockDTO.medicalrecords = new ArrayList<>();
        mockDTO.medicalrecords.add(recordA);
        when(jsonDataRepository.get()).thenReturn(mockDTO);
        medicalRecordRepository = new MedicalRecordRepository(jsonDataRepository);
    }

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        MedicalRecord record = recordB;
        // ACT
        medicalRecordRepository.create(record);
        MedicalRecord result = medicalRecordRepository.read(record.firstName, record.lastName).get();
        // ASSERT
        assertEquals(record, result);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        MedicalRecord record = recordA;
        // ACT
        Executable action = () -> medicalRecordRepository.create(record);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_read_then_value() throws Exception {
        // ARRANGE
        String firstName = recordA.firstName;
        String lastName = recordA.lastName;
        // ACT
        MedicalRecord result = medicalRecordRepository.read(firstName,lastName).get();
        // ASSERT
        assertEquals(recordA, result);
    }

    @Test
    public void when_read_then_empty() throws Exception {
        // ARRANGE
        String firstName = recordB.firstName;
        String lastName = recordB.lastName;
        // ACT
        Optional<MedicalRecord> result = medicalRecordRepository.read(firstName, lastName);
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
        medicalRecordRepository.update(recordA_modified);
        Optional<MedicalRecord> result = medicalRecordRepository.read(firstName, lastName);
        // ASSERT
        assertEquals(recordA_modified, result.get());
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        MedicalRecord record = recordB;
        // ACT
        Executable action = () -> medicalRecordRepository.update(record);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_then_success() throws Exception {
        // ARRANGE
        String firstName = recordA.firstName;
        String lastName = recordA.lastName;
        // ACT
        medicalRecordRepository.delete(firstName, lastName);
        Optional<MedicalRecord> result = medicalRecordRepository.read(firstName, lastName);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        String firstName = recordB.firstName;
        String lastName = recordB.lastName;
        // ACT
        Executable action = () -> medicalRecordRepository.delete(firstName, lastName);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

}
