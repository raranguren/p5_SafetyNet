package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.dto.*;
import com.ricaragas.safetynet.model.MedicalRecord;
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
import java.util.Optional;

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
        return isChild(getAge(person));
    }

    public boolean isChild(int age) {
        return age <= 18; // per project specification, age 18 is to be counted as child
    }

    public boolean isChild(Optional<Integer> age) {
        if (age.isEmpty()) {
            log.warn("No date of birth. Assuming that they are not a child.");
            return false;
        }
        return isChild(age.get());
    }

    public Optional<Integer> getAge(Person person) {
        var birthDateResult = medicalRecordService.getBirthdateByName(person.firstName, person.lastName);
        if (birthDateResult.isEmpty()) return Optional.empty();

        int age = Period.between(birthDateResult.get(), LocalDate.now()).getYears();
        log.info("AGE: {} {} is {} years old.", person.firstName, person.lastName, age);
        return Optional.of(age);
    }

    public ArrayList<ChildAlertPerChildDTO> getChildAlertsByAddress(String address) {
        var result = new ArrayList<ChildAlertPerChildDTO>();
        var persons = personRepository.findAllByAddress(address);
        for (Person person : persons) {
            Optional<Integer> age = getAge(person);
            if (age.isPresent() && isChild(age))  {
                var childAlert = new ChildAlertPerChildDTO();
                childAlert.age = age.get();
                childAlert.firstName = person.firstName;
                childAlert.lastName = person.lastName;
                childAlert.otherResidents = new ArrayList<>();
                for (Person otherResident : persons) {
                    if (!person.equals(otherResident)) {
                        var residentInfo = new ChildAlertPerPersonDTO();
                        residentInfo.firstName = otherResident.firstName;
                        residentInfo.lastName = otherResident.lastName;
                        childAlert.otherResidents.add(residentInfo);
                    }
                }
                result.add(childAlert);
            }
        }
        return result;
    }

    public ArrayList<String> getAllPhoneNumbersByAddress(String address) {
        ArrayList<String> phoneNumbers = new ArrayList<>();
        for (Person person : getPersonsByAddress(address)) {
            phoneNumbers.add(person.phone);
        }
        log.info("Found {} phone numbers for address {}", phoneNumbers.size(), address);
        return phoneNumbers;
    }

    public ArrayList<FireAlertPerPersonDTO> getFireAlertsByAddress(String address) {
        ArrayList<FireAlertPerPersonDTO> residents = new ArrayList<>();
        for (Person person : getPersonsByAddress(address)) {
            var resident = new FireAlertPerPersonDTO();
            resident.lastName = person.lastName;
            resident.phone = person.phone;
            var searchMedicalRecord = medicalRecordService.getByName(person.firstName,person.lastName);
            if (searchMedicalRecord.isPresent()) {
                var medicalRecord = searchMedicalRecord.get();
                resident.allergies = medicalRecord.allergies;
                resident.medications = medicalRecord.medications;
            }
            var age = getAge(person);
            if (age.isPresent()) {
                resident.age = age.get();
            }
            residents.add(resident);
        }
        return residents;
    }

    public Optional<FloodInfoPerAddressDTO> getFloodInfoByAddress(String address) {
        var persons = personRepository.findAllByAddress(address);
        if (persons.isEmpty()) {
            log.info("Skipping address with no persons: " + address);
            return Optional.empty();
        }

        var result = new FloodInfoPerAddressDTO();
        var residents = new ArrayList<FloodInfoPerPersonDTO>();
        for (Person person : persons) {
            var resident = new FloodInfoPerPersonDTO();
            resident.lastName = person.lastName;
            resident.phone = person.phone;
            var searchMedicalRecord = medicalRecordService.getByName(person.firstName,person.lastName);
            if (searchMedicalRecord.isPresent()) {
                var medicalRecord = searchMedicalRecord.get();
                resident.allergies = medicalRecord.allergies;
                resident.medications = medicalRecord.medications;
            }
            var age = getAge(person);
            if (age.isPresent()) {
                resident.age = age.get();
            }
            residents.add(resident);
        }
        result.address = address;
        result.persons = residents;
        return Optional.of(result);
    }

    public Optional<PersonInfoPerPersonDTO> getPersonInfo(String firstName, String lastName) {
        var search = personRepository.read(firstName, lastName);
        if (search.isEmpty()) return Optional.empty();
        var person = search.get();
        var personInfo = new PersonInfoPerPersonDTO();
        personInfo.lastName = person.lastName;
        personInfo.address = person.address;
        personInfo.email = person.email;
        var searchMedicalRecord = medicalRecordService.getByName(firstName, lastName);
        if (searchMedicalRecord.isPresent()) {
            personInfo.allergies = searchMedicalRecord.get().allergies;
            personInfo.medications = searchMedicalRecord.get().medications;
            var age = getAge(person);
            if (age.isPresent()) personInfo.age = age.get();
        }
        return Optional.of(personInfo);
    }
}
