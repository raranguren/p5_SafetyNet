package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FirestationCoveragePerStationDTO {
    private PersonInfo[] coveredPersons;
    private long adultsCount;
    private long childrenCount;

    @Data
    public static class PersonInfo {
        private String firstName, lastName;
        private String address;
        private String phone;
    }
}