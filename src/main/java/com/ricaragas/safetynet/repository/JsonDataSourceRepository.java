package com.ricaragas.safetynet.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.dto.JsonDataSourceDTO;
import com.ricaragas.safetynet.model.MedicalRecord;
import com.ricaragas.safetynet.model.Person;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

// Other repositories inherit from this one
// https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json
@Log4j2
public class JsonDataSourceRepository {

    public static String DATA_URL = "https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json";

    private final JsonDataSourceDTO data;

    // Passing the mapper to the constructor allows unit testing
    public JsonDataSourceRepository(ObjectMapper jsonMapper) {
        try {
            var url = new URL(DATA_URL);
            data = jsonMapper.readValue(url, JsonDataSourceDTO.class);
            log.info("Data retrieved from JSON file");
        } catch (MalformedURLException e) {
            log.fatal("Incorrect URL: " + DATA_URL);
            throw new RuntimeException("Incorrect URL for data source.");
        } catch (IOException e) {
            log.fatal("Could not read JSON file: " + DATA_URL);
            throw new RuntimeException("Could not initialize data structure");
            // Note: Lines with exceptions show no coverage. See JaCoCo FAQ.
        }
    }

    public JsonDataSourceDTO getData() {
        return data;
    }

}
