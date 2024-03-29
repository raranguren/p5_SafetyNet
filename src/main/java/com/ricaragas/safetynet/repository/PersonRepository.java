package com.ricaragas.safetynet.repository;

import com.ricaragas.safetynet.model.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

@Repository
@Log4j2
public class PersonRepository {

    private final ArrayList<Person> persons;

    public PersonRepository(JsonDataRepository jsonDataRepository) {
        persons = jsonDataRepository.get().persons;
        log.debug("Count of records: " + persons.size());
    }

    // CRUD OPERATIONS

    public void create(Person person) throws AlreadyExistsException {
        var index = indexOf(person);
        if (index.isPresent()) {
            String warning = "Unable to create a new record. Another one exists with the same firstName and lastName.";
            log.error(warning);
            throw new AlreadyExistsException(warning);
        }
        persons.add(person);
        log.debug("Created a new record.");
    }

    public Optional<Person> read(String firstName, String lastName) {
        var person = findOne(firstName, lastName);
        log.debug(person.isEmpty() ? "Returning empty result." : "Returning 1 record.");
        return person;
    }

    public void update(Person person) throws NotFoundException {
        var index = indexOf(person);
        if (index.isEmpty()) {
            String warning = "Unable to update a record that doesn't exist";
            log.error(warning);
            throw new NotFoundException(warning);
        }
        persons.set(index.get(),person);
        log.debug("Updated existing record.");
    }

    public void delete(String firstName, String lastName) throws NotFoundException {
        var index = indexOf(firstName, lastName);
        if (index.isEmpty()) {
            String warning = "Unable to delete a record that doesn't exist";
            log.error(warning);
            throw new NotFoundException(warning);
        }
        persons.remove((int)index.get());
    }

    // UTILS

    private Optional<Person> findOne(String firstName, String lastName) {
        return persons.stream()
                .filter(p -> firstName.equals(p.firstName) && lastName.equals(p.lastName))
                .findFirst();
    }

    private Optional<Integer> indexOf(String firstName, String lastName) {
        var searchResult = findOne(firstName, lastName);
        if (searchResult.isEmpty()) {
            log.debug("Record with name " + firstName + " " + lastName + " not found.");
            return Optional.empty();
        }
        int index = persons.indexOf(searchResult.get());
        log.debug("Record with name " + firstName + " " + lastName + " exists with index=" + index);
        return Optional.of(index);
    }

    private Optional<Integer> indexOf(Person person) {
        return indexOf(person.firstName, person.lastName);
    }

    // OTHER QUERIES

    public ArrayList<Person> findAllByAddress(String address) {
        ArrayList<Person> result = new ArrayList<>();
        for (Person person : persons) {
            if (person.address.equals(address)) result.add(person);
        }
        log.debug("Found {} persons with address {}.", result.size(), address);
        return result;
    }

    public ArrayList<Person> findAllByCity(String city) {
        ArrayList<Person> result = new ArrayList<>();
        for (Person person : persons) {
            if (city.equals(person.city)) result.add(person);
        }
        log.debug("Found {} persons in city {}.", result.size(), city);
        return result;
    }

}
