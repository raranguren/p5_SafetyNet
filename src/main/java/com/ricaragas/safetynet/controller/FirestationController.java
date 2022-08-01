package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.FirestationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/firestation")
public class FirestationController {

    private final FirestationService firestationService;
    FirestationController(FirestationService firestationService){
        this.firestationService = firestationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Firestation firestation) throws AlreadyExistsException {
        log.debug("Handling POST request . . .");
        firestationService.create(firestation);
        log.debug("Object created successfully. Returning status 201. (Ok)");
    }

    @PutMapping
    public void update(@RequestBody Firestation firestation) throws NotFoundException {
        log.debug("Handling PUT request . . .");
        firestationService.update(firestation);
        log.debug("Object updated successfully. Returning status 200 (Ok).");
    }

    @DeleteMapping
    public void delete(@RequestBody Firestation firestation) throws NotFoundException {
        log.debug("Handling DELETE request . . .");
        firestationService.delete(firestation);
        log.debug("Object deleted successfully. Returning status 200 (Ok).");
    }

    @ExceptionHandler({AlreadyExistsException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict(){
        log.debug("Action not possible. Returning status 409 (Conflict).");
    }

}
