package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class FireAlertPerPersonDTO {
    public String lastName;
    public String phone;
    public Integer age;
    public ArrayList<String> medications;
    public ArrayList<String> allergies;
}
