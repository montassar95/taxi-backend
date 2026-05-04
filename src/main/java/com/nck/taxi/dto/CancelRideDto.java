package com.nck.taxi.dto;

import lombok.Data;

@Data
public class CancelRideDto {
    private String driverId;
    private String rideId;
    private String reason;
}