package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Service
@Log4j2
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    public void create(MedicalRecord medicalRecord) throws AlreadyExistsException {
        log.info("Forwarding to repository . . .");
        medicalRecordRepository.create(medicalRecord);
    }

    public void update(MedicalRecord medicalRecord) throws NotFoundException {
        log.info("Forwarding to repository . . .");
        medicalRecordRepository.update(medicalRecord);
    }

    public void delete(MedicalRecord medicalRecord) throws NotFoundException {
        log.info("Forwarding to repository . . .");
        medicalRecordRepository.delete(medicalRecord.firstName, medicalRecord.lastName);
    }

    public Optional<MedicalRecord> getByName(String firstName, String lastName) {
        return medicalRecordRepository.read(firstName, lastName);
    }

    public Optional<Integer> getAge(MedicalRecord medicalRecord) {
        String birthdate = medicalRecord.birthdate;
        if (birthdate == null) return Optional.empty();
        LocalDate date;
        try {
            date = LocalDate.parse(birthdate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
        int age = Period.between(date, LocalDate.now()).getYears();
        return Optional.of(age);
    }

}
