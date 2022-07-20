package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FirestationCoveragePerStationDTO {
    private FirestationCoveragePerPersonDTO[] coveredPersons;
    private long adultsCount;
    private long childrenCount;

}