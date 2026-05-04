package com.nck.taxi.dto;

import lombok.Data;

@Data
public class PositionDto {
    private String driverId;
    private Double lat;
    private Double lng;
    private Long timestamp;
}