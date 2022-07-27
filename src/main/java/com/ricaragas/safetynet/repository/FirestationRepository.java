package com.ricaragas.safetynet.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.model.Person;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.ArrayUtils.toMap;

@Repository
@Log4j2
public class FirestationRepository extends JsonDataSourceRepository{

    private final HashMap<String, String> firestations;

    public FirestationRepository(ObjectMapper jsonMapper) {
        super(jsonMapper);
        firestations = new HashMap<>();
        for (Firestation firestation : getData().firestations) {
            firestations.put(firestation.address, firestation.station);
        }
        log.info("Count of records: " + firestations.size());
    }

    // CRUD OPERATIONS

    public void create(Firestation firestation) throws AlreadyExistsException {
        if (firestations.containsKey(firestation.address)) {
            String warning = "Unable to create a new record. Another one exists with the same address.";
            log.warn(warning);
            throw new AlreadyExistsException(warning);
        }
        firestations.put(firestation.address, firestation.station);
        log.info("Created a new record.");
    }

    public Optional<String> read(String address) {
        var station = firestations.get(address);
        log.info(station == null ? "Returning empty result." : "Returning 1 value.");
        return Optional.ofNullable(station);
    }

    public void update(Firestation firestation) throws NotFoundException {
        if (!firestations.containsKey(firestation.address)) {
            String warning = "Unable to update a record that doesn't exist";
            log.warn(warning);
            throw new NotFoundException(warning);
        }
        firestations.put(firestation.address, firestation.station);
        log.info("Updated existing record.");
    }

    public void delete(String address) throws NotFoundException {
        if (!firestations.containsKey(address)) {
            String warning = "Unable to delete a record that doesn't exist";
            log.warn(warning);
            throw new NotFoundException(warning);
        }
        firestations.remove(address);
    }

    // UTILS

    public void deleteAllByStation(String station) {
        firestations.values().removeIf(value -> value.equals(station));
    }

    // OTHER QUERIES

    public ArrayList<String> getAddressesByStationNumber(String stationNumber) {
        ArrayList<String> addresses = new ArrayList<>();
        firestations.forEach((address, station) -> {
            if (station.equals(stationNumber)) {
                addresses.add(address);
            }
        });
        log.info("Found {} addresses covered by station number ", stationNumber);
        return addresses;
    }

}
