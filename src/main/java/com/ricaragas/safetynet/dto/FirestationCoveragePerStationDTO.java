package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class FirestationCoveragePerStationDTO {
    public ArrayList<FirestationCoveragePerPersonDTO> coveredPersons;
    public long adultsCount;
    public long childrenCount;

}