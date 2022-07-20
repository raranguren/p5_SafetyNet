package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FloodInfoPerPersonDTO {
    private String lastName;
    private String phone;
    private int age;
    private String[] medications;
    private String[] allergies;
}
