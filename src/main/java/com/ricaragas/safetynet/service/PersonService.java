package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.dto.FirestationCoveragePerPersonDTO;
import com.ricaragas.safetynet.dto.FirestationCoveragePerStationDTO;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.NotFoundException;
import com.ricaragas.safetynet.repository.PersonRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Log4j2
public class PersonService {

    private final PersonRepository personRepository;
    private final MedicalRecordService medicalRecordService;
    PersonService(PersonRepository personRepository, MedicalRecordService medicalRecordService) {
        this.personRepository = personRepository;
        this.medicalRecordService = medicalRecordService;
    }

    public void create(Person person) throws AlreadyExistsException {
        log.info("Forwarding to repository . . .");
        personRepository.create(person);
    }

    public void update(Person person) throws NotFoundException {
        log.info("Forwarding to repository . . .");
        personRepository.update(person);
    }

    public void delete(Person person) throws NotFoundException {
        log.info("Forwarding to repository . . .");
        personRepository.delete(person.firstName, person.lastName);
    }

    public ArrayList<Person> getPersonsByAddress(String address) {
        log.info("Forwarding to repository . . .");
        return personRepository.findAllByAddress(address);
    }

    public FirestationCoveragePerStationDTO getCoverageReportFromPersonList(ArrayList<Person> persons) {
        ArrayList<FirestationCoveragePerPersonDTO> coveredPersons = new ArrayList<>();
        int adultsCount = 0;
        int childrenCount = 0;
        for (Person person : persons) {
            if (isChild(person)) childrenCount ++;
            var personProfile = new FirestationCoveragePerPersonDTO();
            personProfile.firstName = person.firstName;
            personProfile.lastName = person.lastName;
            personProfile.address = person.address;
            personProfile.phone = person.phone;
            coveredPersons.add(personProfile);
        }
        adultsCount = coveredPersons.size() - childrenCount;

        log.info("Creating report with {} adults and {} children . . .", adultsCount, childrenCount);
        var result = new FirestationCoveragePerStationDTO();
        result.coveredPersons = coveredPersons;
        result.childrenCount = childrenCount;
        result.adultsCount = adultsCount;

        return result;
    }

    public boolean isChild(Person person) {
        var birthDateResult = medicalRecordService.getBirthdateByName(person.firstName, person.lastName);
        if (birthDateResult.isEmpty()) return false;

        int age = Period.between(birthDateResult.get(), LocalDate.now()).getYears();
        log.info("AGE: {} {} is {} years old.", person.firstName, person.lastName, age);
        return age <= 18; // per project specification, age 18 is to be counted as child
    }
}
