package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class ChildAlertPerChildDTO {
    private String firstName, lastName;
    private int age;
    private ChildAlertPerPersonDTO[] otherResidents;
}
