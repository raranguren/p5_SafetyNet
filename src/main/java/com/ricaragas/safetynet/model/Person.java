package com.ricaragas.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class Person {
    public String firstName, lastName;
    public String address;
    public String city;
    public String zip;
    public String phone;
    public String email;
}
