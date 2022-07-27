package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.dto.FirestationCoveragePerStationDTO;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.model.Person;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Log4j2
public class FirestationService {

    private final FirestationRepository firestationRepository;
    private final PersonService personService;
    FirestationService(FirestationRepository firestationRepository, PersonService personService) {
        this.firestationRepository = firestationRepository;
        this.personService = personService;
    }

    public void create(Firestation firestation) throws AlreadyExistsException {
        log.info("Forwarding to repository . . .");
        firestationRepository.create(firestation);
    }

    public void update(Firestation firestation) throws NotFoundException {
        log.info("Forwarding to repository . . .");
        firestationRepository.update(firestation);
    }

    public void delete(Firestation firestation) throws NotFoundException {
        log.info("Forwarding to repository . . .");
        firestationRepository.delete(firestation.address);
    }

    public FirestationCoveragePerStationDTO getCoverageReportByStationNumber(String stationNumber) {
        log.info("Building report of coverage for station number {} . . .", stationNumber);
        ArrayList<String> addresses = firestationRepository.getAddressesByStationNumber(stationNumber);
        ArrayList<Person> persons = new ArrayList<>();
        for (String address : addresses) {
            persons.addAll(personService.getPersonsByAddress(address));
        }
        FirestationCoveragePerStationDTO result = personService.getCoverageReportFromPersonList(persons);
        return result;
    }

}
