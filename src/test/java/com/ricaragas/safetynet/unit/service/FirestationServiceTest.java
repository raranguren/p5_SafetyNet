package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.repository.FirestationRepository;
import com.ricaragas.safetynet.service.FirestationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FirestationServiceTest {

    @InjectMocks
    private FirestationService firestationService;

    @Mock
    private FirestationRepository firestationRepository;

}
