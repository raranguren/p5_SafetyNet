package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.repository.PersonRepository;
import com.ricaragas.safetynet.service.MedicalRecordService;
import com.ricaragas.safetynet.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;
    @Mock
    private MedicalRecordService medicalRecordService;

    // Objects with the same values to verify that the service calls the repository
    Person preparedValue = new Person("AAA","BBB","cc","dd","123","234","a@1.c");
    Person expectedValue = new Person("AAA","BBB","cc","dd","123","234","a@1.c");

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        // ACT
        personService.create(preparedValue);
        // ASSERT
        verify(personRepository, times(1)).create(expectedValue);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        doThrow(AlreadyExistsException.class).when(personRepository).create(any());
        // ACT
        Executable action = () -> personService.create(preparedValue);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_update_then_success() throws Exception {
        // ARRANGE
        // ACT
        personService.update(preparedValue);
        // ASSERT
        verify(personRepository, times(1)).update(expectedValue);
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(personRepository).update(any());
        // ACT
        Executable action = () -> personService.update(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_then_success() throws Exception {
        // ARRANGE
        // ACT
        personService.delete(preparedValue);
        // ASSERT
        verify(personRepository, times(1))
                .delete(expectedValue.firstName, expectedValue.lastName);
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        doThrow(NotFoundException.class).when(personRepository).delete(anyString(), anyString());
        // ACT
        Executable action = () -> personService.delete(preparedValue);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_coverage_report_then_success() {
        // ARRANGE
        ArrayList<Person> persons = new ArrayList<>(List.of(preparedValue));
        LocalDate tenYearsAgo = LocalDate.now().minusYears(10);
        when(medicalRecordService.getBirthdateByName(any(),any()))
                .thenReturn(Optional.of(tenYearsAgo));
        // ACT
        var result = personService.getCoverageReportFromPersonList(persons);
        var resultPersonInfo = result.coveredPersons.get(0);
        // ASSERT
        assertEquals(1, result.childrenCount);
        assertEquals(0, result.adultsCount);
        assertEquals(1, result.coveredPersons.size());
        assertEquals(expectedValue.firstName, resultPersonInfo.firstName);
        assertEquals(expectedValue.lastName, resultPersonInfo.lastName);
        assertEquals(expectedValue.address, resultPersonInfo.address);
        assertEquals(expectedValue.phone, resultPersonInfo.phone);
    }

    @Test
    public void when_get_persons_by_address_then_success() {
        // ARRANGE
        // ACT
        var result = personService.getPersonsByAddress("123abc");
        // ASSERT
        verify(personRepository).findAllByAddress(eq("123abc"));
        assertNotNull(result);
    }

    @Test
    public void when_get_child_alerts_by_address_then_success() {
        // ARRANGE
        String address = preparedValue.address;
        Person child = preparedValue;
        Person adult = new Person("X","Y",address,"","","","");
        LocalDate childBirthdate = LocalDate.now().minusYears(17);
        LocalDate adultBirthdate = LocalDate.now().minusYears(19);

        when(personRepository.findAllByAddress(address))
                .thenReturn(new ArrayList<>(List.of(child,adult)));
        when(medicalRecordService.getBirthdateByName(any(),any()))
                .thenReturn(Optional.of(childBirthdate))
                .thenReturn(Optional.of(adultBirthdate));

        // ACT
        var result = personService.getChildAlertsByAddress(address);
        var resultResidents = result.get(0).otherResidents;
        // ASSERT
        assertEquals(1, result.size());
        assertEquals(1, resultResidents.size());
        assertEquals(17, result.get(0).age);
        assertEquals(preparedValue.firstName, result.get(0).firstName);
        assertEquals(preparedValue.lastName, result.get(0).lastName);
    }

    @Test
    public void when_get_phone_numbers_by_address_then_success() {
        // ARRANGE
        String address = preparedValue.address;
        var persons = new ArrayList<>(List.of(preparedValue));
        when(personRepository.findAllByAddress(address))
                .thenReturn(persons);
        // ACT
        var result = personService.getAllPhoneNumbersByAddress(address);
        // ASSERT
        assertEquals(expectedValue.phone, result.get(0));
        assertEquals(1, result.size());
    }

    @Test
    public void when_get_fire_alerts_by_address_then_success() {
        // ARRANGE
        String address = preparedValue.address;
        var medicalRecord = new MedicalRecord(
                preparedValue.firstName,
                preparedValue.lastName,
                "01/01/2001",
                new ArrayList<>(),
                new ArrayList<>());
        var persons = new ArrayList<>(List.of(preparedValue));
        when(medicalRecordService.getByName(anyString(),anyString()))
                .thenReturn(Optional.of(medicalRecord));
        when(personRepository.findAllByAddress(address))
                .thenReturn(persons);
        // ACT
        var result = personService.getFireAlertsByAddress(address);
        // ASSERT
        verify(medicalRecordService, times(1))
                .getByName(expectedValue.firstName, expectedValue.lastName);
        verify(personRepository, times(1))
                .findAllByAddress(expectedValue.address);
        assertEquals(1, result.size());
    }

    @Test
    public void when_get_fire_alerts_by_address_and_no_medical_records_then_success() {
        // ARRANGE
        String address = preparedValue.address;
        var persons = new ArrayList<>(List.of(preparedValue));
        when(medicalRecordService.getByName(anyString(),anyString()))
                .thenReturn(Optional.empty());
        when(personRepository.findAllByAddress(address))
                .thenReturn(persons);
        // ACT
        var result = personService.getFireAlertsByAddress(address);
        // ASSERT
        verify(medicalRecordService, times(1))
                .getByName(expectedValue.firstName, expectedValue.lastName);
        verify(personRepository, times(1))
                .findAllByAddress(expectedValue.address);
        assertEquals(1, result.size());
    }

    @Test
    public void when_get_flood_info_by_address_then_success() {
        // ARRANGE
        String address = preparedValue.address;
        var medicalRecord = new MedicalRecord(
                preparedValue.firstName,
                preparedValue.lastName,
                "01/01/2001",
                new ArrayList<>(),
                new ArrayList<>());
        var persons = new ArrayList<>(List.of(preparedValue));
        when(medicalRecordService.getByName(anyString(),anyString()))
                .thenReturn(Optional.of(medicalRecord));
        when(personRepository.findAllByAddress(address))
                .thenReturn(persons);
        // ACT
        var result = personService.getFloodInfoByAddress(address);
        // ASSERT
        verify(medicalRecordService, times(1))
                .getByName(expectedValue.firstName, expectedValue.lastName);
        verify(personRepository, times(1))
                .findAllByAddress(expectedValue.address);
        assertEquals(1, result.get().persons.size());
        assertEquals(address, result.get().address);
    }

    @Test
    public void when_get_flood_info_by_address_and_no_medical_records_then_success() {
        // ARRANGE
        String address = preparedValue.address;
        var persons = new ArrayList<>(List.of(preparedValue));
        when(medicalRecordService.getByName(anyString(),anyString()))
                .thenReturn(Optional.empty());
        when(personRepository.findAllByAddress(address))
                .thenReturn(persons);
        // ACT
        var result = personService.getFloodInfoByAddress(address);
        // ASSERT
        verify(medicalRecordService, times(1))
                .getByName(expectedValue.firstName, expectedValue.lastName);
        verify(personRepository, times(1))
                .findAllByAddress(expectedValue.address);
        assertEquals(1, result.get().persons.size());
        assertEquals(address, result.get().address);
    }

}
