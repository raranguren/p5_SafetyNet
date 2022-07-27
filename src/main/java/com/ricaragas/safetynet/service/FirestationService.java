package com.ricaragas.safetynet.service;

import com.ricaragas.safetynet.model.Firestation;
import com.ricaragas.safetynet.repository.AlreadyExistsException;
import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.repository.NotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class FirestationService {

    private final FirestationRepository firestationRepository;
    FirestationService(FirestationRepository firestationRepository) {
        this.firestationRepository = firestationRepository;
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

}
