package com.ricaragas.safetynet.dto;

import lombok.Data;

public class FloodInfoPerAddressDTO {
    public String address;
    public FloodInfoPerPersonDTO[] persons;
}


