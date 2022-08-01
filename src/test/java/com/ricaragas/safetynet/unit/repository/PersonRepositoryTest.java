package com.ricaragas.safetynet.unit.repository;

import com.ricaragas.safetynet.dto.JsonDataDTO;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.JsonDataRepository;
import com.ricaragas.safetynet.repository.PersonRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PersonRepositoryTest {

    @Mock
    JsonDataRepository jsonDataRepository;

    // sample data
    Person personA = new Person("Ada", "Ab", "123 Rue", "Buc", "78000", "123-123", "a@mail.com");
    Person personB = new Person("Bea", "Bec", "23 Rue", "Buc", "78000", "123-123", "a@mail.com");

    private PersonRepository personRepository;

    @BeforeEach
    public void before_each() throws IOException {
        var mockDTO = new JsonDataDTO();
        mockDTO.persons = new ArrayList<>();
        mockDTO.persons.add(personA);
        when(jsonDataRepository.get()).thenReturn(mockDTO);
        personRepository = new PersonRepository(jsonDataRepository);
    }

    @Test
    public void when_create_then_success() throws Exception {
        // ARRANGE
        Person person = personB;
        // ACT
        personRepository.create(person);
        Person result = personRepository.read(person.firstName, person.lastName).get();
        // ASSERT
        assertEquals(person, result);
    }

    @Test
    public void when_create_then_fail() throws Exception {
        // ARRANGE
        Person person = personA;
        // ACT
        Executable action = () -> personRepository.create(person);
        // ASSERT
        assertThrows(AlreadyExistsException.class, action);
    }

    @Test
    public void when_read_then_value() throws Exception {
        // ARRANGE
        String firstName = personA.firstName;
        String lastName = personA.lastName;
        // ACT
        Person result = personRepository.read(firstName,lastName).get();
        // ASSERT
        assertEquals(personA, result);
    }

    @Test
    public void when_read_then_empty() throws Exception {
        // ARRANGE
        String firstName = personB.firstName;
        String lastName = personB.lastName;
        // ACT
        Optional<Person> result = personRepository.read(firstName, lastName);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_update_then_success() throws Exception {
        // ARRANGE
        String firstName = personA.firstName;
        String lastName = personA.lastName;
        Person personA_modified = new Person(firstName, lastName,
                "999 rue","Paris","1000","999-999","m@mail.com");
        // ACT
        personRepository.update(personA_modified);
        Optional<Person> result = personRepository.read(firstName, lastName);
        // ASSERT
        assertEquals(personA_modified, result.get());
    }

    @Test
    public void when_update_then_fail() throws Exception {
        // ARRANGE
        Person person = personB;
        // ACT
        Executable action = () -> personRepository.update(person);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_delete_then_success() throws Exception {
        // ARRANGE
        String firstName = personA.firstName;
        String lastName = personA.lastName;
        // ACT
        personRepository.delete(firstName, lastName);
        Optional<Person> result = personRepository.read(firstName, lastName);
        // ASSERT
        assert(result.isEmpty());
    }

    @Test
    public void when_delete_then_fail() throws Exception {
        // ARRANGE
        String firstName = personB.firstName;
        String lastName = personB.lastName;
        // ACT
        Executable action = () -> personRepository.delete(firstName, lastName);
        // ASSERT
        assertThrows(NotFoundException.class, action);
    }

    @Test
    public void when_find_by_address_then_values() throws Exception {
        // ARRANGE
        String address = personA.address;
        // ACT
        var result = personRepository.findAllByAddress(address);
        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(personA, result.get(0));
    }

    @Test
    public void when_find_by_city_then_values() throws  Exception {
        // ARRANGE
        String city = personA.city;
        // ACT
        var result = personRepository.findAllByCity(city);
        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(personA, result.get(0));
    }

}
