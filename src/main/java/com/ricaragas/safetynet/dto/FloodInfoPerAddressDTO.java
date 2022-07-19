package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FloodInfoPerAddressDTO {
    private String address;
    private PersonInfo[] persons;

    @Data
    public static class PersonInfo {
        private String firstName, lastName;
        private String phone;
        private int age;
        private String[] medications;
        private String[] allergies;
    }
}
