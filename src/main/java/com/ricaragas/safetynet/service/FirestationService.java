package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.repository.FirestationRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FirestationService {

    private FirestationRepository firestationRepository;
    FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
    }

}
