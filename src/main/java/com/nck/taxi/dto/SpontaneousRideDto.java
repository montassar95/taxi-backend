package com.nck.taxi.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SpontaneousRideDto {
    private String driverId;
    private String passengerName;
    private String pickupAddress;
    private String dropoffAddress;
//    private Double pickupLat;
//    private Double pickupLng;
    private LocalDateTime rideDateTime;
    
    // ✅ CHAMP OBLIGATOIRE: "all" ou l'ID du chauffeur
    private String targetDriver; // "all" ou "1" ou "2" etc.
}