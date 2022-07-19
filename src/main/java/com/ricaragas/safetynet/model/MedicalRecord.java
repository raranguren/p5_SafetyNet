package com.ricaragas.safetynet.model;

import lombok.Data;

@Data
public class MedicalRecord {
    private String firstName;
    private String lastName;
    private String birthdate;
    private String[] medications;
    private String[] allergies;
}
