package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class PersonInfoPerNameDTO {
    private String firstName, lastName;
    private String address;
    private int age;
    private String email;
    private String[] medications;
    private String[] allergies;
}
