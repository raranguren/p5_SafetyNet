package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Person person) throws AlreadyExistsException {
        personService.create(person);
    }

    @PutMapping
    public void update(@RequestBody Person person) throws NotFoundException {
        personService.update(person);
    }

    @DeleteMapping
    public void delete(@RequestBody Person person) throws NotFoundException {
        personService.delete(person);
    }

    @ExceptionHandler({AlreadyExistsException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict(){}

}
