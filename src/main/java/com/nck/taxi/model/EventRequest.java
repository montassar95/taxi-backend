package com.nck.taxi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Data
public class EventRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Infos client
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    // Détails événement
    private String eventType;     // MARIAGE, SEMINAIRE, GROUPE, AUTRE
    private LocalDate eventDate;
    private LocalTime startTime;
    private Integer numberOfPersons;
    private Integer numberOfTaxis;
    private String tripType;      // ALLER_SIMPLE, ALLER_RETOUR
    private String comment;

    // Meta
    private LocalDateTime createdAt = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private RequestStatus status = RequestStatus.PENDING;

    private String adminResponse;

    public enum RequestStatus {
        PENDING, ACCEPTED, REFUSED
    }
}