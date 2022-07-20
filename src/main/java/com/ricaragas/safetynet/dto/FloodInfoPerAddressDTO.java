package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FloodInfoPerAddressDTO {
    private String address;
    private FloodInfoPerPersonDTO[] persons;
}


