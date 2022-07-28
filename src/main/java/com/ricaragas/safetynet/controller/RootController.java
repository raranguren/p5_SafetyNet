package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.dto.*;
import com.ricaragas.safetynet.service.FirestationService;
import com.ricaragas.safetynet.service.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping("/")
public class RootController {

    private final FirestationService firestationService;
    private final PersonService personService;
    RootController(FirestationService firestationService, PersonService personService) {
        this.firestationService = firestationService;
        this.personService = personService;
    }

    @GetMapping("firestation")
    public ResponseEntity<?> firestation(@RequestParam("stationNumber") String stationNumber) {
        log.info("Received GET /firestation?stationNumber={} . . .", stationNumber);
        if (stationNumber == null) throwBadRequest();
        var result = firestationService.getCoverageReportByStationNumber(stationNumber);
        log.info("Returning result with status 200 (Ok).");
        return entityWithResultOrEmptyJson(result);
    }

    @GetMapping("childAlert")
    public Iterable<ChildAlertPerChildDTO> childAlert(@RequestParam("address") String address) {
        log.info("Received GET /childAlert?address={} . . .", address);
        if (address == null) throwBadRequest();
        ArrayList<ChildAlertPerChildDTO> result = personService.getChildAlertsByAddress(address);
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    @GetMapping("phoneAlert")
    public Iterable<String> phoneAlert(@RequestParam("firestation") String stationNumber) {
        log.info("Received GET /phoneAlert?firestation={} . . .", stationNumber);
        if (stationNumber == null) throwBadRequest();
        ArrayList<String> result = firestationService.getUniquePhoneNumbersByStationNumber(stationNumber);
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    @GetMapping("fire")
    public ResponseEntity<?> fire(@RequestParam("address") String address) {
        log.info("Received GET /fire?address={} . . . ", address);
        if (address == null) throwBadRequest();
        var result = firestationService.getFireAlertByAddress(address);
        log.info("Returning result with status 200 (Ok).");
        return entityWithResultOrEmptyJson(result);
    }

    @GetMapping("flood/stations")
    public Iterable<FloodInfoPerAddressDTO> flood(@RequestParam("stations") ArrayList<String> stationNumbers) {
        log.info("Received GET /flood/stations?stations={}", stationNumbers);
        if (stationNumbers == null || stationNumbers.size() == 0) throwBadRequest();
        ArrayList<FloodInfoPerAddressDTO> result = firestationService.getFloodInfoByStationNumbers(stationNumbers);
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    @GetMapping("personInfo")
    public PersonInfoPerPersonDTO personInfo(@RequestParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        log.info("Received GET /personInfo?firstName={}&lastName={} . . .", firstName, lastName);
        var result = personService.getPersonInfo(firstName, lastName);
        if (result.isEmpty()) throwBadRequest();
        log.info("Returning result with status 200 (Ok).");
        return result.get();
    }

    @GetMapping("communityEmail")
    public Iterable<String> communityEmail(@RequestParam("city") String city) {
        log.info("Received GET /communityEmail?city={} . . .", city);
        if (city == null) throwBadRequest();
        ArrayList<String> result = personService.getEmailsByCity(city);
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    // UTILS

    private void throwBadRequest() {
        log.info("Returning status 400 (Bad request)");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<?> entityWithResultOrEmptyJson(Optional<?> optional) {
        if (optional.isPresent()) return ResponseEntity.of(optional);
        return ResponseEntity.ok("{}");
    }
}
