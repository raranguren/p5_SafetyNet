package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.service.MedicalRecordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@Log4j2
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody MedicalRecord medicalRecord) throws AlreadyExistsException {
        log.debug("Handling POST request . . .");
        medicalRecordService.create(medicalRecord);
        log.debug("Object created successfully. Returning status 201. (Ok)");
    }

    @PutMapping
    public void update(@RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        log.debug("Handling PUT request . . .");
        medicalRecordService.update(medicalRecord);
        log.debug("Object updated successfully. Returning status 200 (Ok).");
    }

    @DeleteMapping
    public void delete(@RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        log.debug("Handling DELETE request . . .");
        medicalRecordService.delete(medicalRecord);
        log.debug("Object deleted successfully. Returning status 200 (Ok).");
    }

    @ExceptionHandler({AlreadyExistsException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict(){
        log.debug("Action not possible. Returning status 409 (Conflict).");
    }
}
