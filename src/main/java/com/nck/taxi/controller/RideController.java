package com.nck.taxi.controller;

import com.nck.taxi.dto.CancelRideDto;
import com.nck.taxi.dto.RideDto;
import com.nck.taxi.dto.SpontaneousRideDto;
import com.nck.taxi.model.Driver;
import com.nck.taxi.model.Ride;
import com.nck.taxi.service.DriverService;
import com.nck.taxi.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RideController {

    private final RideService rideService;
    private final DriverService driverService;
    
    // Accepter une course
    @PostMapping("/{rideId}/accept")
    public ResponseEntity<Ride> acceptRide(
            @PathVariable Long rideId,
            @RequestBody RideDto dto) {
        try {
            Ride ride = rideService.acceptRide(rideId, Long.parseLong(dto.getDriverId()));
            return ResponseEntity.ok(ride);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Annuler une course
    @PostMapping("/cancel")
    public ResponseEntity<Ride> cancelRide(@RequestBody CancelRideDto dto) {
        return ResponseEntity.ok(rideService.cancelRide(dto));
    }

    // Terminer une course
    @PostMapping("/{rideId}/complete")
    public ResponseEntity<Ride> completeRide(
            @PathVariable Long rideId,
            @RequestBody RideDto dto) {
        return ResponseEntity.ok(
            rideService.completeRide(rideId, Long.parseLong(dto.getDriverId()))
        );
    }

    
    // Démarrer la course
    @PostMapping("/{rideId}/start")
    public ResponseEntity<Ride> startRide(
            @PathVariable Long rideId,
            @RequestBody RideDto dto) {

        return ResponseEntity.ok(
            rideService.startRide(rideId, Long.parseLong(dto.getDriverId()))
        );
    }
    
    // Course spontanée
    @PostMapping("/spontaneous")
    public ResponseEntity<Ride> createSpontaneousRide(
            @RequestBody SpontaneousRideDto dto) {
        return ResponseEntity.ok(rideService.createSpontaneousRide(dto));
    }

    // Historique chauffeur
    @GetMapping("/driver/{driverId}/history")
    public ResponseEntity<List<Ride>> getHistory(@PathVariable Long driverId) {
        return ResponseEntity.ok(rideService.getDriverHistory(driverId));
    }

    
 
    
    // Test — envoie course à tous
    @PostMapping("/test")
    public ResponseEntity<Ride> createTestRide(
            @RequestParam Double pickupLat,
            @RequestParam Double pickupLng,
            @RequestParam String pickupAddress,
            @RequestParam String passengerName) {

        Ride ride = rideService.createRide(
            passengerName, pickupAddress,
            "Destination à confirmer",
            pickupLat, pickupLng
        );
        rideService.broadcastRideToAllDrivers(ride);
        return ResponseEntity.ok(ride);
    }
}