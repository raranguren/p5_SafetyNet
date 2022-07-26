package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.service.MedicalRecordService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class MedicalRecordController {

    private MedicalRecordService medicalRecordService;
    MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

}
