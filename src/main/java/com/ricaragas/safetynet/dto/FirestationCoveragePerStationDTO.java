package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class FirestationCoveragePerStationDTO {
    public ArrayList<FirestationCoveragePerPersonDTO> coveredPersons;
    public Integer adultsCount;
    public Integer childrenCount;

}