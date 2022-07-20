package com.ricaragas.safetynet.dto;

import lombok.Data;

public class ChildAlertPerChildDTO {
    public String firstName, lastName;
    public int age;
    public ChildAlertPerPersonDTO[] otherResidents;
}
