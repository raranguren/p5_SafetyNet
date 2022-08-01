package com.ricaragas.safetynet.repository;

import com.ricaragas.safetynet.model.MedicalRecord;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Optional;

@Repository
@Log4j2
public class MedicalRecordRepository {

    private final ArrayList<MedicalRecord> medicalRecords;

    public MedicalRecordRepository(JsonDataRepository jsonDataRepository) {
        medicalRecords = jsonDataRepository.get().medicalrecords;
        log.debug("Count of records: " + medicalRecords.size());
    }

    // CRUD OPERATIONS

    public void create(MedicalRecord medicalRecord) throws AlreadyExistsException {
        var index = indexOf(medicalRecord);
        if (index.isPresent()) {
            String warning = "Unable to create a new record. Another one exists with the same firstName and lastName.";
            log.error(warning);
            throw new AlreadyExistsException(warning);
        }
        medicalRecords.add(medicalRecord);
        log.debug("Created a new record.");
    }

    public Optional<MedicalRecord> read(String firstName, String lastName) {
        var medicalRecord = findOne(firstName, lastName);
        log.debug(medicalRecord.isEmpty() ? "Returning empty result." : "Returning 1 record.");
        return medicalRecord;
    }

    public void update(MedicalRecord medicalRecord) throws NotFoundException {
        var index = indexOf(medicalRecord);
        if (index.isEmpty()) {
            String warning = "Unable to update a record that doesn't exist";
            log.error(warning);
            throw new NotFoundException(warning);
        }
        medicalRecords.set(index.get(),medicalRecord);
        log.debug("Updated existing record.");
    }

    public void delete(String firstName, String lastName) throws NotFoundException {
        var index = indexOf(firstName, lastName);
        if (index.isEmpty()) {
            String warning = "Unable to delete a record that doesn't exist";
            log.error(warning);
            throw new NotFoundException(warning);
        }
        medicalRecords.remove((int)index.get());
        log.info("Deleted a record");
    }

    // UTILS

    private Optional<MedicalRecord> findOne(String firstName, String lastName) {
        return medicalRecords.stream()
                .filter(p -> firstName.equals(p.firstName) && lastName.equals(p.lastName))
                .findFirst();
    }

    private Optional<Integer> indexOf(String firstName, String lastName) {
        var searchResult = findOne(firstName, lastName);
        if (searchResult.isEmpty()) {
            log.debug("Record with name " + firstName + " " + lastName + " not found.");
            return Optional.empty();
        }
        int index = medicalRecords.indexOf(searchResult.get());
        log.debug("Record with name " + firstName + " " + lastName + " exists with index=" + index);
        return Optional.of(index);
    }

    private Optional<Integer> indexOf(MedicalRecord medicalRecord) {
        return indexOf(medicalRecord.firstName, medicalRecord.lastName);
    }
}
