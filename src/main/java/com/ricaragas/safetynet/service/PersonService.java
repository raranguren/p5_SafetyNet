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
        log.debug("Forwarding to repository . . .");
        personRepository.create(person);
    }

    public void update(Person person) throws NotFoundException {
        log.debug("Forwarding to repository . . .");
        personRepository.update(person);
    }

    public void delete(Person person) throws NotFoundException {
        log.debug("Forwarding to repository . . .");
        personRepository.delete(person.firstName, person.lastName);
    }

    public ArrayList<Person> getPersonsByAddress(String address) {
        log.debug("Forwarding to repository . . .");
        return personRepository.findAllByAddress(address);
    }

    public FirestationCoveragePerStationDTO getCoverageReportFromPersonList(ArrayList<Person> persons) {
        ArrayList<FirestationCoveragePerPersonDTO> coveredPersons = new ArrayList<>();
        int adultsCount = 0;
        int childrenCount = 0;
        for (Person person : persons) {
            if (isChild(getAge(getMedicalRecord(person)))) childrenCount ++;
            var personProfile = new FirestationCoveragePerPersonDTO();
            personProfile.firstName = person.firstName;
            personProfile.lastName = person.lastName;
            personProfile.address = person.address;
            personProfile.phone = person.phone;
            coveredPersons.add(personProfile);
        }
        adultsCount = coveredPersons.size() - childrenCount;

        log.debug("Creating report with {} adults and {} children . . .", adultsCount, childrenCount);
        var result = new FirestationCoveragePerStationDTO();
        result.coveredPersons = coveredPersons;
        result.childrenCount = childrenCount;
        result.adultsCount = adultsCount;

        return result;
    }

    public ArrayList<ChildAlertPerChildDTO> getChildAlertsByAddress(String address) {
        var result = new ArrayList<ChildAlertPerChildDTO>();
        var persons = personRepository.findAllByAddress(address);
        for (Person person : persons) {
            Optional<Integer> age = getAge(getMedicalRecord(person));
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
        log.debug("Found {} phone numbers for address {}", phoneNumbers.size(), address);
        return phoneNumbers;
    }

    public ArrayList<FireAlertPerPersonDTO> getFireAlertsByAddress(String address) {
        ArrayList<FireAlertPerPersonDTO> residents = new ArrayList<>();
        for (Person person : getPersonsByAddress(address)) {
            var resident = new FireAlertPerPersonDTO();
            resident.lastName = person.lastName;
            resident.phone = person.phone;
            var searchMedicalRecord = getMedicalRecord(person);
            if (searchMedicalRecord.isPresent()) {
                var medicalRecord = searchMedicalRecord.get();
                resident.allergies = medicalRecord.allergies;
                resident.medications = medicalRecord.medications;
                var age = getAge(medicalRecord);
                if (age.isPresent()) {
                    resident.age = age.get();
                }
            }
            residents.add(resident);
        }
        return residents;
    }

    public Optional<FloodInfoPerAddressDTO> getFloodInfoByAddress(String address) {
        var persons = personRepository.findAllByAddress(address);
        if (persons.isEmpty()) {
            log.debug("Skipping address with no persons: " + address);
            return Optional.empty();
        }

        var result = new FloodInfoPerAddressDTO();
        var residents = new ArrayList<FloodInfoPerPersonDTO>();
        for (Person person : persons) {
            var resident = new FloodInfoPerPersonDTO();
            resident.lastName = person.lastName;
            resident.phone = person.phone;
            var searchMedicalRecord = getMedicalRecord(person);
            if (searchMedicalRecord.isPresent()) {
                var medicalRecord = searchMedicalRecord.get();
                resident.allergies = medicalRecord.allergies;
                resident.medications = medicalRecord.medications;
                var age = getAge(medicalRecord);
                if (age.isPresent()) {
                    resident.age = age.get();
                }
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
            var age = getAge(getMedicalRecord(person));
            if (age.isPresent()) personInfo.age = age.get();
        }
        return Optional.of(personInfo);
    }

    public ArrayList<String> getEmailsByCity(String city) {
        log.debug("Asking repository for emails in city=" + city);
        var persons = personRepository.findAllByCity(city);
        var result = new ArrayList<String>();
        for (Person person : persons) {
            result.add(person.email);
        }
        return result;
    }

    // HANDLE AGE CALCULATIONS WHEN MEDICAL RECORDS CAN BE MISSING

    private Optional<MedicalRecord> getMedicalRecord(Person person) {
        return medicalRecordService.getByName(person.firstName, person.lastName);
    }

    private Optional<Integer> getAge(MedicalRecord medicalRecord) {
        return medicalRecordService.getAge(medicalRecord);
    }

    private Optional<Integer> getAge(Optional<MedicalRecord> medicalRecord) {
        if (medicalRecord.isEmpty()) return Optional.empty();
        return getAge(medicalRecord.get());
    }

    private boolean isChild(int age) {
        return age <= 18; // per project specification, age 18 is to be counted as child
    }

    public boolean isChild(Optional<Integer> age) {
        if (age.isEmpty()) {
            log.error("No date of birth. Assuming that they are not a child.");
            return false;
        }
        return isChild(age.get());
    }
}
