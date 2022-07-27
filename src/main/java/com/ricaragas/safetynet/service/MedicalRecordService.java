package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public void create(MedicalRecord medicalRecord) throws AlreadyExistsException {
        medicalRecordRepository.create(medicalRecord);
    }

    public void update(MedicalRecord medicalRecord) throws NotFoundException {
        medicalRecordRepository.update(medicalRecord);
    }

    public void delete(MedicalRecord medicalRecord) throws NotFoundException {
        medicalRecordRepository.delete(medicalRecord.firstName, medicalRecord.lastName);
    }
}
