package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MedicalRecordService {

    private MedicalRecordRepository medicalRecordRepository;
    MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

}
