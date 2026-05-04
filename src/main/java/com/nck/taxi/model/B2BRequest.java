package com.nck.taxi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class B2BRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Infos entreprise
    private String companyName;
    private String contactName;
    private String phone;
    private String email;
    private String address;
    private String city;

    // Besoin transport
    private String serviceType;   // EMPLOYES, CLIENTS, LIVRAISON, NAVETTE

    // Volume estimé
    private String dailyRides;    // 1_5, 5_10, 10_PLUS
    private String frequency;     // QUOTIDIEN, HEBDOMADAIRE, OCCASIONNEL
    private String schedule;      // MATIN, JOURNEE, NUIT
    private String usersCount;    // 1_5, 5_20, 20_PLUS

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