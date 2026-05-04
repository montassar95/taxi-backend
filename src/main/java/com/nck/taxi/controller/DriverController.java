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
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DriverController {
 
    private final DriverService driverService;
     
    @PutMapping("/{driverId}/availability")
    public ResponseEntity<Driver> updateAvailability(
            @PathVariable Long driverId,
            @RequestParam boolean available) {

        Driver updatedDriver = driverService.setDriverAvailability(driverId, available);

        return ResponseEntity.ok(updatedDriver);
    } 
}