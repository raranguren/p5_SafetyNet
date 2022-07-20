package com.ricaragas.safetynet.model;

import lombok.Data;

public class MedicalRecord {
    public String firstName, lastName;
    public String birthdate;
    public String[] medications;
    public String[] allergies;
}
