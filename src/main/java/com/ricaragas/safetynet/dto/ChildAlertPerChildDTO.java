package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class ChildAlertPerChildDTO {
    public String firstName, lastName;
    public Integer age;
    public ArrayList<ChildAlertPerPersonDTO> otherResidents;
}
