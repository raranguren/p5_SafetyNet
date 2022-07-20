package com.ricaragas.safetynet.dto;

import lombok.Data;

public class FirestationCoveragePerStationDTO {
    public FirestationCoveragePerPersonDTO[] coveredPersons;
    public long adultsCount;
    public long childrenCount;

}