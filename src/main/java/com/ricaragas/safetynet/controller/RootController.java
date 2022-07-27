package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.dto.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.ArrayList;

@RestController
@Log4j2
@RequestMapping("/")
public class RootController {

    @GetMapping("firestation")
    public FirestationCoveragePerStationDTO firestation(@PathParam("stationNumber") String stationNumber) {
        log.info("Received GET request for the coverage of firestation number " + stationNumber + " . . .");
        var result = new FirestationCoveragePerStationDTO();
        // TODO
        log.info("Returning result with status 200 (Ok).");
        return result;
    }

    @GetMapping("childAlert")
    public Iterable<ChildAlertPerChildDTO> childAlert(@PathParam("address") String address) {
        log.info("Received GET request for the list of children in address " + address + " . . .");
        ArrayList<ChildAlertPerChildDTO> result = new ArrayList<>();
        // TODO
        log.info("Returning " + result.size() + " results with status 200 (Ok).");
        return result;
    }

    @GetMapping("phoneAlert")
    public Iterable<String> phoneAlert(@PathParam("firestation") String stationNumber) {
        log.info("Received GET request for all phone numbers covered by station " + stationNumber + " . . .");
        ArrayList<String> result = new ArrayList<>();
        // TODO
        log.info("Returning " + result.size() + " results with status 200 (Ok).");
        return result;
    }

    @GetMapping("fire")
    public FireAlertPerAddressDTO fire(@PathParam("address") String address) {
        log.info("Received GET request for the fire alert information for address " + address + " . . . ");
        var result = new FireAlertPerAddressDTO();
        // TODO
        log.info("Returning result with status 200 (Ok).");
        return result;
    }

    @GetMapping("flood")
    public Iterable<FloodInfoPerAddressDTO> flood(@PathParam("stations") ArrayList<String> stationNumbers) {
        log.info("Received GET request for the flood alert information for " + stationNumbers.size() + " stations");
        ArrayList<FloodInfoPerAddressDTO> result = new ArrayList<>();
        // TODO
        log.info("Returning " + result.size() + " results with status 200 (Ok).");
        return result;
    }

    @GetMapping("personInfo")
    public PersonInfoPerPersonDTO personInfo(@PathParam("firstName") String firstName, @PathParam("lastName") String lastName) {
        log.info("Received GET request for information about the person with name " + firstName + " " + lastName + " . . .");
        var result = new PersonInfoPerPersonDTO();
        // TODO
        log.info("Returning result with status 200 (Ok).");
        return result;
    }

    @GetMapping("communityEmail")
    public Iterable<String> communityEmail(@PathParam("city") String city) {
        log.info("Received GET request for the list of emails in city " + city + " . . .");
        ArrayList<String> result = new ArrayList<>();
        // TODO
        log.info("Returning " + result.size() + " results with status 200 (Ok).");
        return result;
    }

}
