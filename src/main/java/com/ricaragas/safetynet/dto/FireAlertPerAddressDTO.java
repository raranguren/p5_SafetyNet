package com.ricaragas.safetynet.dto;

import lombok.Data;

@Data
public class FireAlertPerAddressDTO {
    private FireAlertPerPersonDTO[] habitants;
    private String station;
}
