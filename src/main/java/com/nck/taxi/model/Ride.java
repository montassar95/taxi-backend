package com.nck.taxi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private String pickupAddress;
    private String dropoffAddress;
    private Double pickupLat;
    private Double pickupLng;

    @Enumerated(EnumType.STRING)
    private RideStatus status = RideStatus.PENDING;

    @ManyToOne
    private Driver driver;

    // Date/heure de la course
    private LocalDateTime rideDateTime;

    // Date création
    private LocalDateTime createdAt = LocalDateTime.now();

    // Date acceptation
    private LocalDateTime acceptedAt;

    // Date annulation
    private LocalDateTime cancelledAt;

    // Raison annulation
    private String cancellationReason;

    // Course spontanée
    private boolean spontaneous = false;
}