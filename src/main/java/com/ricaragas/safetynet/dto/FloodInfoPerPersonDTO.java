package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class FloodInfoPerPersonDTO {
    public String lastName;
    public String phone;
    public int age;
    public ArrayList<String> medications;
    public ArrayList<String> allergies;
}
