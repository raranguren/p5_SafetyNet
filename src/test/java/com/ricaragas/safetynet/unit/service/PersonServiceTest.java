package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.repository.PersonRepository;
import com.ricaragas.safetynet.service.PersonService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

}
