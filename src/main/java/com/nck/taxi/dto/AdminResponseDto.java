package com.nck.taxi.dto;

import lombok.Data;

@Data
public class AdminResponseDto {
    private String status;   // ACCEPTED, REFUSED, CONFIRMED, CANCELLED
    private String response; // message admin optionnel
}