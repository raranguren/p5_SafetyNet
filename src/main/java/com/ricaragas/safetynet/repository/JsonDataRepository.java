package com.ricaragas.safetynet.repository;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ricaragas.safetynet.dto.JsonDataDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;

// Other repositories inherit from this one
// https://s3-eu-west-1.amazonaws.com/course.oc-static.com/projects/DA+Java+EN/P5+/data.json
@Repository
@Log4j2
public class JsonDataRepository {

    private final JsonDataDTO copyOfJsonValues;

    public JsonDataRepository(ObjectMapper mapper, // Injecting mapper allows unit testing
                              @Value("${json-data-address}") String jsonDataAddress) {
        try {
            var url = new URL(jsonDataAddress);
            copyOfJsonValues = mapper.readValue(url, JsonDataDTO.class);
            log.debug("Data retrieved from JSON file");
        } catch (IOException e) {
            log.fatal("Could not read JSON file: " + jsonDataAddress);
            throw new RuntimeException("Could not initialize data structure");
        }
    }

    public JsonDataDTO get() {
        return copyOfJsonValues;
    }
}
