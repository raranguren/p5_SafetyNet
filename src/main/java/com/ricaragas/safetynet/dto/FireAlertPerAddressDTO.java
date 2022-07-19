package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FireAlertPerAddressDTO {
    private PersonInfo[] habitants;
    private String station;

    @Data
    public static class PersonInfo {
        private String firstName, lastName;
        private String phone;
        private int age;
        private String[] medications;
        private String[] allergies;
    }
}
