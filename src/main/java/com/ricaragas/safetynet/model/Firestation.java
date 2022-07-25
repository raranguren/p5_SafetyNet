package com.ricaragas.safetynet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor  // Jackson's ObjectMapper::readValue needs empty constructor
@AllArgsConstructor // To write shorter tests
@EqualsAndHashCode  // So that assertEquals verifies the values
public class Firestation {
    public String address;
    public String station;
}
