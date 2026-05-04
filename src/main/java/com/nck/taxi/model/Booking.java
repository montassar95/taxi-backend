package com.nck.taxi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Infos client
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    // Détails course
    private String pickupAddress;
    private String dropoffAddress;
    private LocalDateTime bookingDateTime;
    private String tripType; // ALLER_SIMPLE, ALLER_RETOUR
    private String comment;

    // Meta
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED
    }
}