package com.ricaragas.safetynet.unit.service;

import com.ricaragas.safetynet.repository.MedicalRecordRepository;
import com.ricaragas.safetynet.service.MedicalRecordService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MedicalRecordServiceTest {

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

}
