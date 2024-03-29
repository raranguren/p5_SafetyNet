package com.ricaragas.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor  // Jackson's ObjectMapper::readValue needs empty constructor
@AllArgsConstructor // To write shorter tests
@EqualsAndHashCode  // So that assertEquals verifies the values
public class MedicalRecord {
    public String firstName, lastName;
    public String birthdate;
    public ArrayList<String> medications;
    public ArrayList<String> allergies;
}
