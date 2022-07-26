package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class ChildAlertPerChildDTO {
    public String firstName, lastName;
    public int age;
    public ArrayList<ChildAlertPerPersonDTO> otherResidents;
}
