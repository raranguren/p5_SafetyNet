package com.ricaragas.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor  // Jackson's ObjectMapper::readValue needs empty constructor
@AllArgsConstructor // To write shorter tests
@EqualsAndHashCode  // So that assertEquals verifies the values
public class Person {
    public String firstName, lastName;
    public String address;
    public String city;
    public String zip;
    public String phone;
    public String email;
}
