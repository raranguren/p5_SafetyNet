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
        medicalRecordService.create(medicalRecord);
    }

    @PutMapping
    public void update(@RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        medicalRecordService.update(medicalRecord);
    }

    @DeleteMapping
    public void delete(@RequestBody MedicalRecord medicalRecord) throws NotFoundException {
        medicalRecordService.delete(medicalRecord);
    }

    @ExceptionHandler({AlreadyExistsException.class, NotFoundException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleConflict(){}
}
