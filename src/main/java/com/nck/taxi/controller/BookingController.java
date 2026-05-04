package com.nck.taxi.controller;

import com.nck.taxi.dto.BookingRequest;
import com.nck.taxi.dto.AdminResponseDto;
import com.nck.taxi.model.Booking;
import com.nck.taxi.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> create(@RequestBody BookingRequest dto) {
        return ResponseEntity.ok(bookingService.create(dto));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAll() {
        return ResponseEntity.ok(bookingService.getAll());
    } 

    @PutMapping("/{id}/status")
    public ResponseEntity<Booking> updateStatus(
            @PathVariable Long id,
            @RequestBody AdminResponseDto dto) {
        return ResponseEntity.ok(bookingService.updateStatus(id, dto));
    }
}