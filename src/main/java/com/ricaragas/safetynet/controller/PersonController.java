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
        log.debug("Handling POST request . . .");
        personService.create(person);
        log.debug("Object created successfully. Returning status 201. (Ok)");
    }

    @PutMapping
    public void update(@RequestBody Person person) throws NotFoundException {
        log.debug("Handling PUT request . . .");
        personService.update(person);
        log.debug("Object updated successfully. Returning status 200 (Ok).");
    }

    @DeleteMapping
    public void delete(@RequestBody Person person) throws NotFoundException {
        log.debug("Handling DELETE request . . .");
        personService.delete(person);
        log.debug("Object deleted successfully. Returning status 200 (Ok).");
    }

    @ExceptionHandler({AlreadyExistsException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict(){
        log.debug("Action not possible. Returning status 409 (Conflict).");
    }

}
