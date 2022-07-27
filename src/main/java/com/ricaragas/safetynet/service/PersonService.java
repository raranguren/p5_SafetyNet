package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PersonService {

    private final PersonRepository personRepository;
    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void create(Person person) throws AlreadyExistsException {
        personRepository.create(person);
    }

    public void update(Person person) throws NotFoundException {
        personRepository.update(person);
    }

    public void delete(Person person) throws NotFoundException {
        personRepository.delete(person.firstName, person.lastName);
    }
}
