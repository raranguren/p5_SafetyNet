package com.ricaragas.safetynet.controller;

import com.ricaragas.safetynet.service.FirestationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Log4j2
public class FirestationController {

    private FirestationService firestationService;
    FirestationController(FirestationService firestationService){
        this.firestationService = firestationService;
    }

}
