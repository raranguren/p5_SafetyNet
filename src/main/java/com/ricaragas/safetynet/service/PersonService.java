package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PersonService {

    private PersonRepository personRepository;
    PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
