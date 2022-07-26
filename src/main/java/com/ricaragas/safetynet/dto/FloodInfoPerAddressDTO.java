package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class FloodInfoPerAddressDTO {
    public String address;
    public ArrayList<FloodInfoPerPersonDTO> persons;
}


