package com.nck.taxi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nck.taxi.b2b.model.B2BCompany;
import com.nck.taxi.b2b.model.B2BCourse;

@Entity
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String passengerName;
    private String pickupAddress;
    private String dropoffAddress;

    @Enumerated(EnumType.STRING)
    private RideStatus status = RideStatus.PENDING;

    @ManyToOne
    @JsonIgnoreProperties({"password", "rides"})   // évite Driver → Ride → Driver
    private Driver driver;

    private LocalDateTime rideDateTime;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime acceptedAt;
    private LocalDateTime cancelledAt;
    private String cancellationReason;
    private boolean spontaneous = false;
    private boolean b2b = false;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b2b_source_id")
    @JsonIgnore                                    // ← coupe Ride → B2BCourse → Ride
    private B2BCourse b2bSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "b2b_company_id")
    @JsonIgnore                                    // ← pas utile dans le JSON du Ride
    private B2BCompany b2bCompany;
}