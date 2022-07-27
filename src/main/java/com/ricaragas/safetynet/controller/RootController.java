package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.dto.*;
import com.ricaragas.safetynet.service.FirestationService;
import com.ricaragas.safetynet.service.PersonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.websocket.server.PathParam;
import java.util.ArrayList;

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
    public FirestationCoveragePerStationDTO firestation(@PathParam("stationNumber") String stationNumber) {
        log.info("Received GET /firestation?stationNumber={} . . .", stationNumber);
        if (stationNumber == null) throwBadRequest();
        var result = firestationService.getCoverageReportByStationNumber(stationNumber);
        log.info("Returning result with status 200 (Ok).");
        return result;
    }

    @GetMapping("childAlert")
    public Iterable<ChildAlertPerChildDTO> childAlert(@PathParam("address") String address) {
        log.info("Received GET /childAlert?address={} . . .", address);
        ArrayList<ChildAlertPerChildDTO> result = new ArrayList<>();
        // TODO
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    @GetMapping("phoneAlert")
    public Iterable<String> phoneAlert(@PathParam("firestation") String stationNumber) {
        log.info("Received GET /phoneAlert/firestation={} . . .", stationNumber);
        ArrayList<String> result = new ArrayList<>();
        // TODO
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    @GetMapping("fire")
    public FireAlertPerAddressDTO fire(@PathParam("address") String address) {
        log.info("Received GET /fire?address={} . . . ", address);
        var result = new FireAlertPerAddressDTO();
        // TODO
        log.info("Returning result with status 200 (Ok).");
        return result;
    }

    @GetMapping("flood")
    public Iterable<FloodInfoPerAddressDTO> flood(@PathParam("stations") ArrayList<String> stationNumbers) {
        log.info("Received GET /flood?stations={}", stationNumbers);
        ArrayList<FloodInfoPerAddressDTO> result = new ArrayList<>();
        // TODO
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    @GetMapping("personInfo")
    public PersonInfoPerPersonDTO personInfo(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        log.info("Received GET /personInfo?firstName={}&lastName={} . . .", firstName, lastName);
        var result = new PersonInfoPerPersonDTO();
        // TODO
        log.info("Returning result with status 200 (Ok).");
        return result;
    }

    @GetMapping("communityEmail")
    public Iterable<String> communityEmail(@PathParam("city") String city) {
        log.info("Received GET /communityEmail?city={} . . .", city);
        ArrayList<String> result = new ArrayList<>();
        // TODO
        log.info("Returning {} results with status 200 (Ok).", result.size());
        return result;
    }

    // UTILS

    private void throwBadRequest() {
        log.info("Returning status 400 (Bad request)");
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
