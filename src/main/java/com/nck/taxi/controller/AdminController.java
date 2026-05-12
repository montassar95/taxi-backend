package com.nck.taxi.controller;

import com.nck.taxi.dto.SpontaneousRideDto;
import com.nck.taxi.model.Driver;
import com.nck.taxi.model.Ride;
import com.nck.taxi.service.DriverService;
import com.nck.taxi.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final RideService   rideService;
    private final DriverService driverService;

    // ── Stats dashboard ──
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<Ride>   allRides   = rideService.getAllRides();
        List<Driver> allDrivers = driverService.getAllDrivers();

        long pending   = allRides.stream().filter(r -> r.getStatus().name().equals("PENDING")).count();
        long accepted  = allRides.stream().filter(r -> r.getStatus().name().equals("ACCEPTED")).count();
        long inProgress= allRides.stream().filter(r -> r.getStatus().name().equals("IN_PROGRESS")).count();
        long done      = allRides.stream().filter(r -> r.getStatus().name().equals("DONE")).count();
        long cancelled = allRides.stream().filter(r -> r.getStatus().name().equals("CANCELLED")).count();
        long available = allDrivers.stream().filter(Driver::isAvailable).count();

        return ResponseEntity.ok(Map.of(
            "totalRides",      allRides.size(),
            "pendingRides",    pending,
            "acceptedRides",   accepted,
            "inProgressRides", inProgress,
            "doneRides",       done,
            "cancelledRides",  cancelled,
            "totalDrivers",    allDrivers.size(),
            "availableDrivers",available
        ));
    }

    // ── Tous les chauffeurs ──
    @GetMapping("/drivers")
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    // ── Toutes les courses ──
    @GetMapping("/rides")
    public ResponseEntity<List<Ride>> getAllRides() {
        return ResponseEntity.ok(rideService.getAllRides());
    }

    // ── ✅ CRÉER ET DISTRIBUER (Broadcast OU Assignation selon sélection) ──
    @PostMapping("/rides/create")
    public ResponseEntity<Ride> createAndBroadcast(
            @RequestBody SpontaneousRideDto dto) {
        
        // Créer la course
        Ride ride = rideService.createRide(
            dto.getPassengerName(),
            dto.getPickupAddress(),
            dto.getDropoffAddress() 
//            dto.getPickupLat(),
//            dto.getPickupLng()
        );

        // ✅ IF/ELSE CONDITIONNEL
        if ("all".equalsIgnoreCase(dto.getTargetDriver())) {
            // CAS 1: Broadcast à TOUS les chauffeurs
            rideService.broadcastRideToAllDrivers(ride);
            System.out.println("✅ Course #" + ride.getId() + " → BROADCAST à TOUS");
        } else {
            // CAS 2: Envoyer UNIQUEMENT à un chauffeur spécifique
            try {
                Long driverId = Long.parseLong(dto.getTargetDriver());
                rideService.sendRideToDriver(driverId, ride);
                System.out.println("✅ Course #" + ride.getId() + " → Chauffeur #" + driverId);
            } catch (NumberFormatException e) {
                System.err.println("❌ ID chauffeur invalide: " + dto.getTargetDriver());
            }
        }

        return ResponseEntity.ok(ride);
    }

    // ── Endpoint séparé (optionnel, peut rester) ──
    @PostMapping("/rides/{rideId}/assign/{driverId}")
    public ResponseEntity<Ride> assignToDriver(
            @PathVariable Long rideId,
            @PathVariable Long driverId) {
        
        Ride ride = rideService.getRideById(rideId);
        rideService.sendRideToDriver(driverId, ride);
        System.out.println("✅ Course #" + rideId + " → Assignée au chauffeur #" + driverId);
        
        return ResponseEntity.ok(ride);
    }
}