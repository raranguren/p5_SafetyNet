package com.ricaragas.safetynet.model;

import lombok.Data;

@Data
public class Person {
    private String firstName, lastName;
    private String address;
    private String city;
    private String zip;
    private String phone;
    private String email;
}
