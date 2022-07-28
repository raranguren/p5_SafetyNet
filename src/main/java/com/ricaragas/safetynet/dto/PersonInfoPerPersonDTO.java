package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class PersonInfoPerPersonDTO {
    public String lastName;
    public String address;
    public Integer age;
    public String email;
    public ArrayList<String> medications;
    public ArrayList<String> allergies;
}
