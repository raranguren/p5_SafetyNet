package com.ricaragas.safetynet.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.dto.JsonDataSourceDTO;
import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.model.Person;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// Other repositories inherit from this one
// https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json
@Log4j2
public class JsonDataSourceRepository {

    public static String DATA_URL = "https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json";

    protected List<Person> persons;
    protected List<Firestation> firestations;
    protected List<MedicalRecord> medicalRecords;

    JsonDataSourceRepository() {
        persons = new ArrayList<>();
        firestations = new ArrayList<>();
        medicalRecords = new ArrayList<>();
        try {
            var url = new URL(DATA_URL);
            var jsonMapper = new ObjectMapper();
            var data = jsonMapper.readValue(url, JsonDataSourceDTO.class);
            log.info("Data retrieved from JSON file");
            persons = List.of(data.persons);
            firestations = List.of(data.firestations);
            medicalRecords = List.of(data.medicalrecords);
        } catch (MalformedURLException e) {
            log.fatal("Incorrect URL: " + DATA_URL);
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("Could not read JSON file: " + DATA_URL);
            log.warn("Continuing with an empty data source");
        }
        log.info("Data ready: "
                + persons.size() + " persons, "
                + firestations.size() + " fire stations, and "
                + medicalRecords.size() + " medical records.");
    }

}
