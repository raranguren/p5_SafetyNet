package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class ChildAlertPerChildDTO {
    private String firstName, lastName;
    private int age;
    private PersonInfo[] otherResidents;

    public static class PersonInfo {
        private String firstName, lastName;
    }
}
