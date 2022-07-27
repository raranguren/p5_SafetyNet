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
        firestationService.create(firestation);
    }

    @PutMapping
    public void update(@RequestBody Firestation firestation) throws NotFoundException {
        firestationService.update(firestation);
    }

    @DeleteMapping
    public void delete(@RequestBody Firestation firestation) throws NotFoundException {
        firestationService.delete(firestation);
    }

    @ExceptionHandler({AlreadyExistsException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict(){}

}
