package com.ricaragas.safetynet.dto;

import lombok.Data;

import java.util.ArrayList;

public class FireAlertPerAddressDTO {
    public ArrayList<FireAlertPerPersonDTO> habitants;
    public String station;
}
