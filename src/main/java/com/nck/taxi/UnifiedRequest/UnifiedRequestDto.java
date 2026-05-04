package com.nck.taxi.UnifiedRequest;

 
import lombok.Data;

/**
 * DTO unifié retourné par /api/requests.
 * Tous les statuts sont normalisés : PENDING | CONFIRMED | CANCELLED
 * (les ACCEPTED/REFUSED internes ne sortent jamais du backend).
 */
@Data
public class UnifiedRequestDto {

    private Long   id;
    private String type;      // "booking" | "event" | "b2b"
    private String status;    // PENDING | CONFIRMED | CANCELLED  (normalisé)
    private String createdAt;

    // ── Champs communs ──────────────────────────────────────────────
    private String name;   // passengerName (booking/event) ou contactName (b2b)
    private String phone;
    private String email;
    private String comment;

    // ── Booking ─────────────────────────────────────────────────────
    private String pickup;
    private String dropoff;
    private String rideDate;
    private String tripType;

    // ── Event ───────────────────────────────────────────────────────
    private String  eventType;
    private String  eventDate;
    private String  startTime;
    private Integer numberOfPersons;
    private Integer numberOfTaxis;

    // ── B2B ─────────────────────────────────────────────────────────
    private String companyName;
    private String contactName;
    private String address;
    private String city;
    private String serviceType;
    private String dailyRides;
    private String frequency;
    private String schedule;
    private String usersCount;
}
